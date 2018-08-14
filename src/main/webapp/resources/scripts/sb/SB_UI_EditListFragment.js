/**************************************************************************
    Class - UI - Edit Scene Fragment

**************************************************************************/
var SB_UI_EditListFragment = function (container, config, builder, communicator)
{
    this.container = container;
    this.config = config;
    this.builder = builder;
    this.communicator = communicator;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_EditListFragment");

    this.htmlElements = {};
};
SB_UI_EditListFragment.VISIBILITY_SPEED = "fast";




SB_UI_EditListFragment.prototype.buildUI = function ()
{
    // Self-reference
    var that = this;

    console.log(this.fragment);

    // Clear any old content
    this.clear();

    // Element - Name field
    this.htmlElements.input_name = DOMUtils.input("", this.fragment.name, "text");

    // Element - Asset Set select
    this.htmlElements.select_assetSet = DOMUtils.select(
        "asset_set",
        this.config.assetSets,
        function (x) { return x.id; },
        function (x) { return x.name; },
        this.fragment.assetSet.id);

    // Element - Stylesheet select
    this.htmlElements.select_stylesheet = DOMUtils.select(
        "stylesheet",
        this.config.stylesheets,
        function (x) { return x.id; },
        function (x) { return x.name; },
        this.fragment.stylesheet.id);

    // Component - Stylesheet description panel
    this.htmlElements.div_stylesheet_description = DOMUtils.div(
        "description",
        "",
        this.config.stylesheets[0] && this.config.stylesheets[0].description ? this.config.stylesheets[0].description : "No description available");

    // Action - Update stylesheet description panel
    var stylesheetMap = _.indexBy(this.config.stylesheets, "id");
    this.htmlElements.select_stylesheet.onchange = function ()
    {
        // Get description
        var selectedIndex = this.selectedIndex != -1 ? this.selectedIndex : 0;
        var desc = stylesheetMap[this.options[selectedIndex].value].description;

        // Update description panel
        if (desc)
            that.htmlElements.div_stylesheet_description.innerHTML = desc;
        else
            that.htmlElements.div_stylesheet_description.innerHTML = "No description available";
    };

    // Element - Asset List div
    this.htmlElements.assetListContainer = DOMUtils.div("assets");
    this.htmlElements.assetCheckboxes = [];
    this.buildUI_assetList(this.config.assetSets[this.htmlElements.select_assetSet.selectedIndex]);

    // Element - Buttons
    var button_save = DOMUtils.button("", "", "Save Changes", function () { that._evt_saveChanges(); });
    var button_cancel = DOMUtils.button("", "", "Cancel", function () { that._evt_closeForm(); });
    var button_selectAll = DOMUtils.button("", "", "Select All", function () { that._evt_selectAll(); });
    var button_selectNone = DOMUtils.button("", "", "Select None", function () { that._evt_selectNone(); });

    // Assemble - Form
    var glue_upper_form = DOMUtils.div("glue", "", "");
    glue_upper_form.appendChild(DOMUtils.label("Name"));
    glue_upper_form.appendChild(this.htmlElements.input_name);
    glue_upper_form.appendChild(document.createElement("hr"));
    glue_upper_form.appendChild(DOMUtils.label("Stylesheet"));
    glue_upper_form.appendChild(this.htmlElements.select_stylesheet);
    glue_upper_form.appendChild(this.htmlElements.div_stylesheet_description);
    glue_upper_form.appendChild(DOMUtils.label("Asset Set"));
    glue_upper_form.appendChild(this.htmlElements.select_assetSet);
    var glue_lower_form = DOMUtils.div("glue", "", "");
    glue_lower_form.appendChild(this.htmlElements.assetListContainer);

    // Assemble - Upper Buttons
    var glue_upper_buttons = DOMUtils.div("glue_center", "", "");
    glue_upper_buttons.appendChild(button_selectAll);
    glue_upper_buttons.appendChild(button_selectNone);

    // Assemble - Lower Buttons
    var glue_lower_buttons = DOMUtils.div("glue_center", "", "");
    glue_lower_buttons.appendChild(button_save);
    glue_lower_buttons.appendChild(button_cancel);

    // Assemble
    this.container.appendChild(DOMUtils.header(1, "Edit List-based Scene Fragment"));
    this.container.appendChild(glue_upper_form);
    this.container.appendChild(glue_upper_buttons);
    this.container.appendChild(glue_lower_form);
    this.container.appendChild(glue_lower_buttons);
};




SB_UI_EditListFragment.prototype.buildUI_assetList = function (assetSet)
{
    // Self-reference
    var that = this;

    // Clear old content
    while (this.htmlElements.assetListContainer.firstChild)
        this.htmlElements.assetListContainer.removeChild(this.htmlElements.assetListContainer.firstChild);
    this.htmlElements.assetCheckboxes = [];

    // Show working message
    this.htmlElements.assetListContainer.innerHTML = "Loading..";

    if (assetSet.assetsLoaded)
        this.buildUI_assetList_postLoad(assetSet);
    else
        this.communicator
            .invoke_asset__loadByAssetSet(assetSet.id)
            .done(function ( data ) {
                if (data.success)
                {
                    console.log("Config - Assets loaded for " + assetSet.name);
                    console.log(data);

                    assetSet.assets = data.assets;
                    assetSet.assetsLoaded = true;

                    that.buildUI_assetList_postLoad(assetSet);
                }
                else
                    throw "Fatal error - could not load assets for " + assetSet.name;
            });
};




SB_UI_EditListFragment.prototype.buildUI_assetList_postLoad = function (assetSet)
{
    // Clear old content
    while (this.htmlElements.assetListContainer.firstChild)
        this.htmlElements.assetListContainer.removeChild(this.htmlElements.assetListContainer.firstChild);

    // Build map of assets included in fragment
    var assetsInFragmentMap = {};
    for (var i = 0 ; i < this.fragment.members.length ; i ++)
        assetsInFragmentMap[this.fragment.members[i].asset.id] = true;

    console.log(this.fragment);

    for (var i = 0 ; i < assetSet.assets.length ; i++)
    {
        // Element - Row
        var row = DOMUtils.div("asset_row " + (i % 2 == 0 ? "even" : "odd"));

        // Element - Label
        var label = DOMUtils.label(assetSet.assets[i].name);

        // Element - Checkbox.
        var input = DOMUtils.input("asset_" + assetSet.assets[i].id, "", "checkbox");
        input.assetId = assetSet.assets[i].id;
        if (assetsInFragmentMap[input.assetId])
            input.checked = true;

        // Assemble
        row.appendChild(label);
        row.appendChild(input);
        this.htmlElements.assetListContainer.appendChild(row);

        // Store
        this.htmlElements.assetCheckboxes[i] = input;
    }
};




SB_UI_EditListFragment.prototype.open = function (fragment)
{
    if (this.isVisible())
        console.log("Popup form already open. Ignoring request to Edit Scene Fragment");
    else
    {
        // Store fragment for use
        this.fragment = fragment;

        this.buildUI();
        this.show();
    }
};




SB_UI_EditListFragment.prototype._evt_closeForm = function ()
{
    // Self reference
    var that = this;

    // Close and clear form
    this.hide(function () { that.clear(); });
};




SB_UI_EditListFragment.prototype._evt_saveChanges = function ()
{
    // Self reference
    var that = this;

    // Create query parameter array
    var assetIds = [];
    for (var i = 0 ; i < this.htmlElements.assetCheckboxes.length ; i++)
    {
        if (this.htmlElements.assetCheckboxes[i].checked)
            assetIds.push(this.htmlElements.assetCheckboxes[i].assetId);
    }

    console.log(assetIds);

    // Pass to builder for execution
    this.builder.updateListFragment(
            this.fragment,
            this.htmlElements.input_name.value,
            this.htmlElements.select_assetSet.value,
            this.htmlElements.select_stylesheet.value,
            assetIds);

    // Close and clear form
    this.hide(function () { that.clear(); });
};




SB_UI_EditListFragment.prototype._evt_selectAll = function ()
{
    for (var i = 0 ; i < this.htmlElements.assetCheckboxes.length ; i ++)
        this.htmlElements.assetCheckboxes[i].checked = true;
};




SB_UI_EditListFragment.prototype._evt_selectNone = function ()
{
    for (var i = 0 ; i < this.htmlElements.assetCheckboxes.length ; i ++)
        this.htmlElements.assetCheckboxes[i].checked = false;
};
