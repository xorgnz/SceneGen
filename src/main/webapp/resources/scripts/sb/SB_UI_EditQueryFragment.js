/**************************************************************************
    Class - UI - Edit Scene Fragment

**************************************************************************/
var SB_UI_EditQueryFragment = function (container, config, builder)
{
    this.container = container;
    this.config = config;
    this.builder = builder;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_EditQueryFragment");

    this.htmlElements = {};
};




SB_UI_EditQueryFragment.prototype.buildUI = function ()
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

    // Element - Query select
    this.htmlElements.select_query = DOMUtils.select(
        "query",
        this.config.queries,
        function (x) { return x.id; },
        function (x) { return x.name; },
        this.fragment.query.id);

    // Component - Query description panel
    this.htmlElements.div_query_description = DOMUtils.div(
        "description",
        "",
        this.config.queries[0] ? this.config.queries[0].description : "No description available");

    // Action - Update query description panel and parameter fields
    var queryMap = _.indexBy(this.config.queries, "id");
    this.htmlElements.select_query.onchange = function ()
    {
        if (this.selectedIndex >= 0)
        {
            var query = queryMap[this.options[this.selectedIndex].value];

            // Get description
            var desc = query.description;

            // Update description panel
            if (desc)
                that.htmlElements.div_query_description.innerHTML = desc;
            else
                that.htmlElements.div_query_description.innerHTML = "No description available";

            // Update parameter fields
            that.buildUI_parameterFields(query);
        }
    };

    // Element - Query parameter div
    this.htmlElements.queryParamContainer = DOMUtils.div("", "query_params");
    this.htmlElements.queryParams = [];
    this.buildUI_parameterFields(queryMap[this.htmlElements.select_query.options[this.htmlElements.select_query.selectedIndex].value]);

    // Element - Buttons
    var button_save = DOMUtils.button("", "", "Save Changes", function () { that._evt_saveChanges(); });
    var button_cancel = DOMUtils.button("", "", "Cancel", function () { that._evt_closeForm(); });

    // Assemble - Form
    var glue_form = DOMUtils.div("glue", "", "");
    glue_form.appendChild(DOMUtils.label("Name"));
    glue_form.appendChild(this.htmlElements.input_name);
    glue_form.appendChild(document.createElement("br"));
    glue_form.appendChild(DOMUtils.label("Asset Set"));
    glue_form.appendChild(this.htmlElements.select_assetSet);
    glue_form.appendChild(DOMUtils.label("Stylesheet"));
    glue_form.appendChild(this.htmlElements.select_stylesheet);
    glue_form.appendChild(this.htmlElements.div_stylesheet_description);
    glue_form.appendChild(document.createElement("hr"));
    glue_form.appendChild(DOMUtils.label("Query"));
    glue_form.appendChild(this.htmlElements.select_query);
    glue_form.appendChild(this.htmlElements.div_query_description);
    glue_form.appendChild(this.htmlElements.queryParamContainer);

    // Assemble - Buttons
    var glue_buttons = DOMUtils.div("glue_center", "", "");
    glue_buttons.appendChild(button_save);
    glue_buttons.appendChild(button_cancel);

    // Assemble
    this.container.appendChild(DOMUtils.header(1, "Edit Query-based Scene Fragment"));
    this.container.appendChild(glue_form);
    this.container.appendChild(glue_buttons);
};




SB_UI_EditQueryFragment.prototype.buildUI_parameterFields = function (query)
{
    // Clear old fields
    while (this.htmlElements.queryParamContainer.firstChild)
        this.htmlElements.queryParamContainer.removeChild(this.htmlElements.queryParamContainer.firstChild);
    this.htmlElements.queryParams = [];

    // Create new elements
    for (var i = 0 ; i < query.parameters.length ; i++)
    {
        // Element - Parameter input.
        // - Preserve value from previous input, if it exists
        // - Use default value provided by fragment
        // - Apply text completion
        var input = DOMUtils.input(query.parameters[i].variable, "", "text");
        if (i < this.htmlElements.queryParams.length)
            input.value = this.htmlElements.queryParams[i].value;
        if (! input.value)
            input.value = this.fragment.queryParamValues[i].value;
        if (_.contains(query.parameters[i].tags, "entityName"))
            SG_TextCompletion.applyTextCompletion(input);

        // Assemble
        this.htmlElements.queryParamContainer.appendChild(DOMUtils.label(query.parameters[i].label));
        this.htmlElements.queryParamContainer.appendChild(input);

        // Store
        this.htmlElements.queryParams[i] = input;
    }
};




SB_UI_EditQueryFragment.prototype.open = function (fragment)
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




SB_UI_EditQueryFragment.prototype._evt_closeForm = function ()
{
    // Self reference
    var that = this;

    // Close and clear form
    this.hide(function () { that.clear(); });
};




SB_UI_EditQueryFragment.prototype._evt_saveChanges = function ()
{
    // Self reference
    var that = this;

    // Create query parameter array
    var queryParams = [];
    for (var i = 0 ; i < this.htmlElements.queryParams.length ; i++)
    {
        queryParams[i] = {
            name: this.htmlElements.queryParams[i].name,
            value: this.htmlElements.queryParams[i].value
        };
    }

    // Pass to builder for execution
    this.builder.updateQueryFragment(
            this.fragment,
            this.htmlElements.input_name.value,
            this.htmlElements.select_assetSet.value,
            this.htmlElements.select_stylesheet.value,
            this.htmlElements.select_query.value,
            queryParams);

    // Close and clear form
    this.hide(function () { that.clear(); });
};



