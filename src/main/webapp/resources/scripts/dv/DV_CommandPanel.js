/**************************************************************************
    Class - AE Focus Panel

    Shows information concerning current exploration focus
    Provides UI controls to shift focus

**************************************************************************/
var DV_CommandPanel = function (container, persistor, visualizer, config)
{
    // Components
    this.container = container;
    this.config = config;
    this.persistor = persistor;
    this.visualizer = visualizer;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "DV_CommandPanel");

    // Create UI Elements
    this.htmlElements = {};
};




// Create the UI for the picking panel
DV_CommandPanel.prototype.buildUI = function ()
{
    // Clear panel
    this.clear();

    // Component - Title
    var div_header = DOMUtils.header(1, "Configure Visualization");

    // Blocks
    var div_step1 = this.buildUI_step1_loadScene();
    var div_step2 = this.buildUI_step2_loadData();
    var div_step3 = this.buildUI_step3_loadStylesheet();

    // Assemble - Complete
    this.container.appendChild(div_header);
    this.container.appendChild(div_step1);
    this.container.appendChild(div_step2);
    this.container.appendChild(div_step3);
};




DV_CommandPanel.prototype.buildUI_step1_loadScene = function ()
{
    // Self reference
    var that = this;

    // Component - Working notification
    this.htmlElements.div_wn_loadScene = DOMUtils.div("workingnotification");
    $(this.htmlElements.div_wn_loadScene).hide();

    // Component - Current scene
    this.htmlElements.div_current_scene = DOMUtils.div("current", "", "No scene loaded");

    // Component - Scene selector
    var scenes = this.config.getScenes();
    scenes = _.sortBy(scenes, function (o) { return o.name; });
    this.htmlElements.select_loadScene = DOMUtils.select(
        "scene_load",
        scenes,
        function (o) { return o.id; },
        function (o) { return o.name; });

    // Component - Button
	this.htmlElements.button_loadScene = DOMUtils.button("","","Load scene");
    this.htmlElements.button_loadScene.onclick = function () { that._evt_loadScene(); };

    // Assemble
    var div = DOMUtils.div("box");
    div.appendChild(this.htmlElements.div_wn_loadScene);
    div.appendChild(DOMUtils.header(2, "Scene"));
    div.appendChild(DOMUtils.div("label", "", "Current scene:"));
    div.appendChild(this.htmlElements.div_current_scene);
    div.appendChild(this.htmlElements.select_loadScene);
    div.appendChild(this.htmlElements.button_loadScene);

    return div;
};




DV_CommandPanel.prototype.buildUI_step2_loadData = function ()
{
    // Self reference
    var that = this;

    // Component - Working notification
    this.htmlElements.div_wn_loadData = DOMUtils.div("workingnotification");
    $(this.htmlElements.div_wn_loadData).hide();

    // Component - Current query data
    this.htmlElements.div_current_data = DOMUtils.div("current", "", "No query results loaded");

    // Component - Query selector
    var queries = this.config.getQueries();
    queries = _.sortBy(queries, function (o) { return o.name; });
    this.htmlElements.select_loadQuery = DOMUtils.select(
        "query_load",
        queries,
        function (o) { return o.id; },
        function (o) { return o.name; });

    // Component - Query description panel
    this.htmlElements.fieldset_query_description = DOMUtils.fieldset(
        "description",
        "",
        queries[0] && queries[0].description ? queries[0].description : "No description available",
        "Description");

    // Action - update query description panel
    var queryMap = _.indexBy(queries, "id");
    this.htmlElements.select_loadQuery.onchange = function ()
    {
        if (this.selectedIndex >= 0)
        {
            var query = queryMap[this.options[this.selectedIndex].value];

            // Get description
            var desc = query.description;

            // Update description panel
            if (desc)
                that.htmlElements.fieldset_query_description.innerHTML = desc;
            else
                that.htmlElements.fieldset_query_description.innerHTML = "No description available";

            // Update parameter fields
            that.buildUI_step2_loadData_parameterFields(query);
        }
    };

    // Element - Query parameter div
    this.htmlElements.queryParamContainer = DOMUtils.fieldset("query_params", "", "");

    console.log(this.htmlElements.queryParamContainer);
    this.htmlElements.queryParams = [];
    this.buildUI_step2_loadData_parameterFields(queryMap[this.htmlElements.select_loadQuery.options[this.htmlElements.select_loadQuery.selectedIndex].value]);

    // Component - Button
	this.htmlElements.button_loadData = DOMUtils.button("","","Load Data");
    this.htmlElements.button_loadData.onclick = function () { that._evt_loadData(); };

    // Assemble
    var div = DOMUtils.div("box");
    div.appendChild(this.htmlElements.div_wn_loadData);
    div.appendChild(DOMUtils.header(2, "Data"));
    div.appendChild(DOMUtils.div("label", "", "Data loaded:"));
    div.appendChild(this.htmlElements.div_current_data);
    div.appendChild(this.htmlElements.select_loadQuery);
    div.appendChild(this.htmlElements.fieldset_query_description);
    div.appendChild(this.htmlElements.queryParamContainer);
    div.appendChild(this.htmlElements.button_loadData);

    return div;
};




DV_CommandPanel.prototype.buildUI_step2_loadData_parameterFields = function (query)
{
    // Clear old fields
    while (this.htmlElements.queryParamContainer.firstChild)
        this.htmlElements.queryParamContainer.removeChild(this.htmlElements.queryParamContainer.firstChild);
    this.htmlElements.queryParams = [];

    // Create fieldset label
    var legend = document.createElement("legend");
    legend.appendChild(document.createTextNode("Parameters:"));
    this.htmlElements.queryParamContainer.appendChild(legend);

    // Create new elements
    for (var i = 0 ; i < query.parameters.length ; i++)
    {
        if (! query.parameters[i].flag_entityId)
        {
            // Element - Parameter input.
            // - Preserve value from previous input, if it exists
            // - Use default value provided by fragment
            // - Apply text completion
            var input = DOMUtils.input(query.parameters[i].variable, "", "text");
            if (i < this.htmlElements.queryParams.length)
                input.value = this.htmlElements.queryParams[i].value;
            if (_.contains(query.parameters[i].tags, "entityName"))
                SG_TextCompletion.applyTextCompletion(input);

            // Assemble
            this.htmlElements.queryParamContainer.appendChild(DOMUtils.label(query.parameters[i].label));
            this.htmlElements.queryParamContainer.appendChild(input);

            // Store
            this.htmlElements.queryParams[i] = input;
        }
    }

    // Show / hide, according to whether params are needed
    if (this.htmlElements.queryParams.length > 0)
        $(this.htmlElements.queryParamContainer).show();
    else
        $(this.htmlElements.queryParamContainer).hide();
};




DV_CommandPanel.prototype.buildUI_step3_loadStylesheet = function ()
{
    // Self reference
    var that = this;

    // Prepare stylesheet list
    var stylesheets = this.config.getStylesheets();
    stylesheets = _.sortBy(stylesheets, function (o) { return o.name; });

    // Component - Working notification
    this.htmlElements.div_wn_loadStylesheet = DOMUtils.div("workingnotification");
    $(this.htmlElements.div_wn_loadStylesheet).hide();

    // Component - Current
    this.htmlElements.div_current_stylesheet = DOMUtils.div("current", "", "No stylesheet loaded");

    // Component - Stylesheet selector
    this.htmlElements.select_loadStylesheet = DOMUtils.select(
        "stylesheet_load",
        stylesheets,
        function (o) { return o.id; },
        function (o) { return o.name; });

    // Component - Query description panel
    this.htmlElements.fieldset_stylesheet_description = DOMUtils.fieldset(
        "description",
        "",
        stylesheets[0] && stylesheets[0].description ? stylesheets[0].description : "No description available",
        "Description");

    // Action - update query description panel
    var stylesheetMap = _.indexBy(stylesheets, "id");
    this.htmlElements.select_loadStylesheet.onchange = function ()
    {
        var selectedIndex = this.selectedIndex != -1 ? this.selectedIndex : 0;
        var desc = stylesheetMap[this.options[selectedIndex].value].description;

        if (desc)
            that.htmlElements.fieldset_stylesheet_description.innerHTML = desc;
        else
            that.htmlElements.fieldset_stylesheet_description.innerHTML = "No description available";
    };

    // Component - Button - Perform action
	this.htmlElements.button_loadStylesheet = DOMUtils.button("","","Apply Stylesheet");
    this.htmlElements.button_loadStylesheet.onclick = function () { that._evt_loadStylesheet(); };

    var div = DOMUtils.div("box");
    div.appendChild(this.htmlElements.div_wn_loadStylesheet);
    div.appendChild(DOMUtils.header(2, "Stylesheet"));
    div.appendChild(DOMUtils.div("label", "", "Current stylesheet:"));
    div.appendChild(this.htmlElements.div_current_stylesheet);
    div.appendChild(this.htmlElements.select_loadStylesheet);
    div.appendChild(this.htmlElements.fieldset_stylesheet_description);
    div.appendChild(this.htmlElements.button_loadStylesheet);

    return div;
};




DV_CommandPanel.prototype._disableUI = function ()
{
    // Selects
    this.htmlElements.select_loadScene.disabled = true;
    this.htmlElements.select_loadQuery.disabled = true;
    this.htmlElements.select_loadStylesheet.disabled = true;

    // Buttons
    $(this.htmlElements.button_loadScene).addClass("disabled");
    $(this.htmlElements.button_loadData).addClass("disabled");
    $(this.htmlElements.button_loadStylesheet).addClass("disabled");
};




DV_CommandPanel.prototype._enableUI = function ()
{
    // Selects
    this.htmlElements.select_loadScene.disabled = false;
    this.htmlElements.select_loadQuery.disabled = false;
    this.htmlElements.select_loadStylesheet.disabled = false;

    // Buttons
    $(this.htmlElements.button_loadScene).removeClass("disabled");
    $(this.htmlElements.button_loadData).removeClass("disabled");
    $(this.htmlElements.button_loadStylesheet).removeClass("disabled");
};




DV_CommandPanel.prototype._evt_loadData = function ()
{
    // Self reference
    var that = this;

    var queryId = that.htmlElements.select_loadQuery.value;
    var queryName = that.htmlElements.select_loadQuery.selectedOptions[0].label;

    // Create query parameter array
    var queryParams = [];
    for (var i = 0 ; i < this.htmlElements.queryParams.length ; i++)
    {
        queryParams[i] = {
            name: this.htmlElements.queryParams[i].name,
            value: this.htmlElements.queryParams[i].value
        };
    }

    // Show working notification
    $(this.htmlElements.div_wn_loadData).text("Working..").fadeIn();

    // Disable UI
    this._disableUI();

    // Initiate load
    this.visualizer.loadData(
        queryId,
        queryParams,
        function()
        {
            // Update loaded data field
            that.htmlElements.div_current_data.innerHTML = "";
            that.htmlElements.div_current_data.appendChild(DOMUtils.text(queryName));
            that.htmlElements.div_current_data.appendChild(DOMUtils.br());
            for (var i = 0 ; i < queryParams.length ; i ++)
                that.htmlElements.div_current_data.appendChild(DOMUtils.div(
                        "",
                        "",
                        queryParams[i].name + ": " + queryParams[i].value,
                        "margin-left: 12px;"));

            // Re-enable UI
            that._enableUI();

            // Update working notification
            $(that.htmlElements.div_wn_loadData).fadeOut();
        });
};




DV_CommandPanel.prototype._evt_loadScene = function ()
{
    // Self reference
    var that = this;

    // Show working notification
    $(this.htmlElements.div_wn_loadScene).text("Working..").fadeIn();

    // Disable UI
    this._disableUI();

    this.visualizer.loadScene(
        that.htmlElements.select_loadScene.value,
        function()
        {
            // Update current scene field
            that.htmlElements.div_current_scene.innerHTML = that.htmlElements.select_loadScene.selectedOptions[0].label;

            // Re-enable UI
            that._enableUI();

            // Update working notification
            $(that.htmlElements.div_wn_loadScene).fadeOut();
        });
};




DV_CommandPanel.prototype._evt_loadStylesheet = function ()
{
    // Self reference
    var that = this;

    // Show working notification
    $(this.htmlElements.div_wn_loadStylesheet).text("Working..").fadeIn();

    // Disable UI
    this._disableUI();

    // Initiate load
    that.visualizer.changeStylesheet(
        that.htmlElements.select_loadStylesheet.value,
        function()
        {
            // Update current stylesheet info box
            that.htmlElements.div_current_stylesheet.innerHTML = that.htmlElements.select_loadStylesheet.selectedOptions[0].label;

            // Re-enable UI
            that._enableUI();

            // Hide working notification
            $(that.htmlElements.div_wn_loadStylesheet).fadeOut();
        });

};
