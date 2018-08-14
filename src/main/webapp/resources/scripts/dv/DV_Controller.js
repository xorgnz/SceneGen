var DV_Controller = function (main)
{
    // Self reference
    var that = this;

    // Store for later calls
    this.main = main;

    // Create components
    this.lens = new DV_Lens(main.sceneManager, this.entityRegistry);
    this.persistor = new DV_Persistor(main.communicator, main.sceneManager, main.config, this.lens);
    this.visualizer = new DV_Visualizer(main.communicator, this.lens, main.sceneManager);

    // Create UI Elements
    this.htmlElements =
    {
        ui_left:        document.getElementById("dv_ui_left_panel"),
        ui_right:       document.getElementById("dv_ui_right_panel"),
        commandPanel:   document.getElementById("dv_command"),
        infoPanel:      document.getElementById("dv_info"),
        legendPanel:    document.getElementById("dv_legend"),
        saveButton:     document.getElementById("dv_save_button"),
        savePanel:      document.getElementById("dv_save_panel"),
    };
    this.uiElements =
    {
        ui_left:        new DV_UI_Region(this.htmlElements.ui_left),
        ui_right:       new DV_UI_Region(this.htmlElements.ui_right),
        commandPanel:   new DV_CommandPanel(this.htmlElements.commandPanel, this.persistor, this.visualizer, this.main.config),
        infoPanel:      new DV_InfoPanel(this.htmlElements.infoPanel, this.lens, main.sceneManager, this.visualizer),
        legendPanel:    new DV_LegendPanel(this.htmlElements.legendPanel, main.sceneManager),
        savePanel:      new DV_SavePanel(this.htmlElements.savePanel, main.config, this.persistor),
    };

    // Wire panel UIs to update on state change events
    this.lens.addListener       ( function() { that.uiElements.infoPanel.buildUI(); }, DV_Lens.LISTENER_SELECT_CHANGE);
    this.visualizer.addListener (
        function() { if (that.uiElements.legendPanel.isVisible()) that.uiElements.legendPanel.buildUI(); },   DV_Visualizer.VISUALIZER_STYLESHEET_CHANGE
    );

    this.visualizer.addListener (
        function() { if (that.uiElements.legendPanel.isVisible()) that.uiElements.legendPanel.buildUI(); },   DV_Visualizer.VISUALIZER_SCENE_CHANGE
    );

    this.visualizer.addListener (
        function() { if (that.uiElements.legendPanel.isVisible()) that.uiElements.legendPanel.buildUI(); },   DV_Visualizer.VISUALIZER_DATA_CHANGE
    );


    // Wire save button to save panel
    this.htmlElements.saveButton.onclick = function ()
    {
        that.uiElements.savePanel.buildUI();
        that.uiElements.savePanel.toggleVisibility();
    };
};




// Help Item configuration
DV_Controller.prototype.helpConfig =
{
    "ui":
    {
        items:
        {
            "C": "Show / hide Configuration panel",
            "L": "Show / hide Legend Panel"
        }
    },
};



// Interface - Controller
// Handle keyboard down events
DV_Controller.prototype.handleKeyDown = function (e, state)
{
    // C - Toggle right panel
    if (e.keyCode == CGA_KeyCodes.KEY_C)
    {
        if (! this.uiElements.ui_right.isVisible())
        {
            this.uiElements.infoPanel.buildUI();
        }

        this.uiElements.ui_right.toggleVisibility();
    }

    // L - Toggle legend panel
    if (e.keyCode == CGA_KeyCodes.KEY_L)
    {
        if (! this.uiElements.legendPanel.isVisible())
            this.uiElements.legendPanel.buildUI();
        else
            this.uiElements.legendPanel.enabled = false;

        this.uiElements.legendPanel.toggleVisibility();
    }
};




// Interface - Controller
// Mouse has been clicked while hovering over an object
DV_Controller.prototype.handleObjectClicked = function (obj)
{
    if (obj)
        this.lens.setSelectedEntity(obj.id);
    else
        this.lens.setSelectedEntity(null);
};




// Interface - Controller
// Mouse has stopped hovering over an object
DV_Controller.prototype.handleObjectHovered = function (obj)
{
    // Set entity as 'hovered'
    if (obj)
        this.lens.setHoveredEntity(obj.id);
    else
        this.lens.setHoveredEntity(null);
};
