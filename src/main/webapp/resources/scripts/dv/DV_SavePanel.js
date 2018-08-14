/**************************************************************************
    Class - AE Settings Panel

    Shows controls for configuration of the Anatomy Explorer

**************************************************************************/
var DV_SavePanel = function (container, config, persistor)
{
    // Save components
    this.container = container;
    this.config = config;
    this.persistor = persistor;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "DV_SavePanel");

    // Create UI Elements
    this.htmlElements = {};

    // Build UI and show
    this.buildUI();
    this.hide();
};




// Create the UI for the picking panel
DV_SavePanel.prototype.buildUI = function ()
{
    // Clear contents
    this.clear();

    // Prepare scene list
    var scenes = this.config.getScenes();
    _.sortBy(scenes, function (o) { return o.name; });

    // Section - Header
    this.container.appendChild(DOMUtils.header(1, "Load / Save Exploration Scenes"));

    this.container.appendChild(this.buildUI_saveNewScene());
    this.container.appendChild(this.buildUI_saveExistingScene(scenes));
};




DV_SavePanel.prototype.buildUI_saveExistingScene = function (scenes)
{
    // Self reference
    var that = this;

    // Container div
    var div = DOMUtils.div("", "saveExistingScene");

    // Component - Scene selector
    scenes = _.sortBy(scenes, function (o) { return o.name; });
    this.htmlElements.select_scene_save = DOMUtils.select(
        "scene_save",
        scenes,
        function (o) { return o.id; },
        function (o) { return o.name; });

    // Component - Button - Perform action
	var button = DOMUtils.button("","","Save to existing scene");
    button.onclick = function ()
    {
        $(button).hide();
        $(div_working_notification).show().text("Working...");

        that.persistor.saveExistingScene(
            that.htmlElements.select_scene_save.value,
            function()
            {
                $(button).show();
                $(div_working_notification).text("Scene saved as '" + that.persistor.sceneName + "'").delay(5000).fadeOut();
            });
    };

    // Component - working notification
    var div_working_notification = DOMUtils.div("working_notification");

    // Assemble Section
    div.appendChild(DOMUtils.header(2, "Save visible entities over an existing scene"));
    div.appendChild(DOMUtils.text("This will delete the scene's current contents.", false, false, "color: red;"));
    var div_glue = DOMUtils.div("glue");
    div_glue.appendChild(this.htmlElements.select_scene_save);
    div_glue.appendChild(DOMUtils.br());
    div_glue.appendChild(button);
    div_glue.appendChild(div_working_notification);
    div.appendChild(div_glue);

    return div;
};




DV_SavePanel.prototype.buildUI_saveNewScene = function ()
{
    // Self reference
    var that = this;

    // Container div
    var div = DOMUtils.div("", "saveNewScene");

    // Component - working notification
    var div_working_notification = DOMUtils.div("working_notification");

    // Component - Button - Perform action
	var button = DOMUtils.button("","","Save as new scene");
    button.onclick = function ()
    {
        $(button).hide();
        $(div_working_notification).show().text("Working...");

        that.persistor.saveNewScene(function()
        {
            $(button).show();
            $(div_working_notification).text("Scene saved as '" + that.persistor.sceneName + "'").delay(5000).fadeOut();
        });
    };

    // Assemble Section
    div.appendChild(DOMUtils.header(2, "Save visible entities into a new scene"));
    var div_glue = DOMUtils.div("glue");
    div_glue.appendChild(button);
    div_glue.appendChild(div_working_notification);
    div.appendChild(div_glue);

    return div;
};

