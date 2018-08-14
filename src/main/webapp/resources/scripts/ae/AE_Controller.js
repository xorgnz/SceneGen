var AE_Controller = function (main)
{
    // Self reference
    var that = this;

    // Store for later calls
    this.main = main;

    // Create components
    this.historian = new AE_Historian();
    this.entityRegistry = new AE_EntityRegistry();
    this.lens = new AE_Lens(main.scene, this.entityRegistry);
    this.explorer = new AE_Explorer(main, this.historian, this.lens, this.entityRegistry);
    this.persistor = new AE_Persistor(main.communicator, main.scene, main.config, this.explorer, this.lens);

    // Create UI Elements
    this.htmlElements =
    {
        clearButton: document.getElementById("ae_clear_button"),
        commandPanel: document.getElementById("ae_command"),
        drillPanel: document.getElementById("ae_drill"),
        errorPanel: document.getElementById("ae_error"),
        historyButton: document.getElementById("ae_history_button"),
        historyPanel: document.getElementById("ae_history"),
        pickingPanel: document.getElementById("ae_picking"),
        explorationPanel: document.getElementById("ae_exploration"),
        settingsPanel: document.getElementById("ae_settings"),
        saveButton: document.getElementById("ae_save_button"),
        savePanel: document.getElementById("ae_save_panel"),
    };
    this.uiElements =
    {
        commandPanel: new AE_CommandPanel(this.htmlElements.commandPanel, this, this.main.config),
        drillPanel: new AE_DrillPanel(this.htmlElements.drillPanel, this),
        errorPanel: new AE_ErrorPanel(this.htmlElements.errorPanel),
        historyPanel: new AE_HistoryPanel(this.htmlElements.historyPanel, this),
        pickingPanel: new AE_PickingPanel(this.htmlElements.pickingPanel, this.lens, this.entityRegistry),
        explorationPanel: new AE_ExplorationPanel(this.htmlElements.explorationPanel, this),
        settingsPanel: new AE_SettingsPanel(this.htmlElements.settingsPanel, this),
        savePanel: new AE_SavePanel(this.htmlElements.savePanel, main.config, this.persistor),
    };

    // Wire panel UIs to update on state change events
    this.historian.addListener  ( function() { that.uiElements.historyPanel.buildUI(); });
    this.explorer.addListener   ( function(data, type) { that.uiElements.explorationPanel.consumeEvent(data, type); });
    this.explorer.addListener   ( function(data, type) { that.uiElements.drillPanel.consumeEvent(data, type); });
    this.explorer.addListener   ( function(data, type) { that.uiElements.errorPanel.consumeEvent(data, type); });
    main.scene.addListener      ( function(data, type) { that.uiElements.explorationPanel.consumeEvent(data, type); });
    main.scene.addListener      ( function(data, type) { that.uiElements.drillPanel.consumeEvent(data, type); });

    // Wire save button to save panel
    this.htmlElements.saveButton.onclick = function ()
    {
        that.uiElements.savePanel.buildUI();
        that.uiElements.savePanel.toggleVisibility();
    };

    this.htmlElements.clearButton.onclick = function ()
    {
        main.scene.clearScene();
    };
    this.htmlElements.historyButton.onclick = function ()
    {
        that.uiElements.historyPanel.buildUI();
        that.uiElements.historyPanel.toggleVisibility();
    };
};




// Help Item configuration
AE_Controller.prototype.helpConfig =
{
    "ui":
    {
        items:
        {
                "H": "Show / hide history panel"
            ,   "X": "Show / hide exploration panel"
//            ,   "S": "Show / Hide settings panel"
        }
    },
    "explorer":
    {
        name: "Anatomy Explorer",
        description: "The following controls allow you to explore the Foundational Model of Anatomy (FMA)",
        items:
        {
                "C": "Clear exploration history"
            ,   "Up / Down Arrow": "Move through exploration history"
        }
    }
};



// Interface - Controller
// Handle keyboard down events
AE_Controller.prototype.handleKeyDown = function (e, state)
{
    // C - Clear current exploration
    if (e.keyCode == CGA_KeyCodes.KEY_C)
    {
        // TEMP
        var focusedEntityIds = [];
        for (var i = 0 ; i < this.focusedObjectDescriptors.length ; i ++)
            focusedEntityIds[this.focusedObjectDescriptors[i].id] = true;
        this.main.scene.filterObjects(focusedEntityIds);

        // Rebuild focus panel to refresh selects
        this.uiElements.commandPanel.buildUI();
    }

    // H - Show history panel
    if (e.keyCode == CGA_KeyCodes.KEY_H)
    {
        if (! this.uiElements.historyPanel.isVisible())
        {
            this.uiElements.historyPanel.buildUI();
            this.uiElements.settingsPanel.hide();
            this.main.cgaApp.controllers.base.uiElements.helpPanel.hide();
        }

        this.uiElements.historyPanel.toggleVisibility();
    }

    // L - Toggle Relations panel
    if (e.keyCode == CGA_KeyCodes.KEY_X)
        this.uiElements.explorationPanel.toggleVisibility();

    // S - Toggle Settings panel
    // if (e.keyCode == CGA_KeyCodes.KEY_S)
    // {
    //     // Hide other panels
    //     if (! this.uiElements.settingsPanel.isVisible())
    //     {
    //         this.uiElements.historyPanel.hide();
    //         this.main.cgaApp.controllers.base.uiElements.helpPanel.hide();
    //     }
    //
    //     this.uiElements.settingsPanel.toggleVisibility();
    // }

    // Left / Right - Move through history
    if (e.keyCode == CGA_KeyCodes.KEY_UP || e.keyCode == CGA_KeyCodes.KEY_DOWN)
    {
        // Move history cursor
        if (e.keyCode == CGA_KeyCodes.KEY_DOWN)
            this.historian.moveCursorBack();
        else
            this.historian.moveCursorForward();

        // Get event under cursor
        var event = this.historian.getEventAtCursor();

        // Perform event
        if (event.type == AE_Historian.EVENT_EXPLORE)
        {
            this.explorer.exploreEntity(event.entityId);
            this.lens.setSelectedEntity(event.entityId);
        }
        else
            this.explorer.exploreEntityRelationPair(event.entityId, event.relation, event.relationLabel);
    }
};




// Interface - Controller
// Mouse has been clicked while hovering over an object
AE_Controller.prototype.handleObjectClicked = function (obj)
{
    if (obj)
    {
        this.lens.setSelectedEntity(obj.id);
        this.explorer.exploreEntity(obj.id);
    }
    else
    {
        this.lens.setSelectedEntity(null);
        this.explorer.exploreEntity(null);
    }
};




// Interface - Controller
// Mouse has stopped hovering over an object
AE_Controller.prototype.handleObjectHovered = function (obj)
{
    // Set entity as 'hovered'
    if (obj)
        this.lens.setHoveredEntity(obj.id);
    else
        this.lens.setHoveredEntity(null);
};
