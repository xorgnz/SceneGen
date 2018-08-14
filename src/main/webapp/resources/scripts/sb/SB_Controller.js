/**************************************************************************
    Class - SceneBuilder That
**************************************************************************/
// Declare object
var SB_Controller = function(mainApp, scene, lens, builder)
{
    // Self reference
    var that = this;

    // Store for later calls
    this.mainApp = mainApp;
    this.scene = scene;
    this.lens = lens;

    // Create UI Elements
    this.htmlElements =
    {
        popupForm:              document.getElementById("sb_popup"),
        linkPanel:              document.getElementById("sb_link"),
        linkButton:             document.getElementById("sb_link_button"),
        objectInfo:             document.getElementById("sb_object_info"),
        overview:               document.getElementById("sb_overview"),
        workingNotification:    document.getElementById("sb_working"),
    };
    this.uiElements =
    {
        addListFragmentPopup:       new SB_UI_AddListFragment(
                                        this.htmlElements.popupForm,
                                        this.mainApp.config,
                                        this.mainApp.builder,
                                        this.mainApp.communicator),
        addQueryFragmentPopup:      new SB_UI_AddQueryFragment(
                                        this.htmlElements.popupForm,
                                        this.mainApp.config,
                                        this.mainApp.builder),
        deleteSceneFragmentPopup:   new SB_UI_DeleteSceneFragment(this.htmlElements.popupForm, this.mainApp.builder),
        editQueryFragmentPopup:     new SB_UI_EditQueryFragment(
                                        this.htmlElements.popupForm,
                                        this.mainApp.config,
                                        this.mainApp.builder),
        editListFragmentPopup:     new SB_UI_EditListFragment(
                                        this.htmlElements.popupForm,
                                        this.mainApp.config,
                                        this.mainApp.builder,
                                        this.mainApp.communicator),
        editScenePopup:             new SB_UI_EditScene(this.htmlElements.popupForm, scene, this.mainApp.builder),
        linkPanel:                  new SB_UI_LinkPanel(this.htmlElements.linkPanel, this.mainApp.config),
        overview:                   new SB_UI_Overview(this.htmlElements.overview, builder, scene, this, this.mainApp.config),
        objectInfo:                 new SB_UI_ObjectInfo(this.htmlElements.objectInfo, builder),
        workingNotification:        new SB_UI_WorkingNotification(this.htmlElements.workingNotification),
    };

    // Wire up components
    builder.addListener(function (data) { that.uiElements.workingNotification.update(data); }, SB_Builder.LISTENER_WORK_STATE);
    scene.addListener(function (data) { that.uiElements.overview.buildUI(); });
    lens.addListener(function (data) { that.uiElements.objectInfo.selectChange(data); }, SB_Lens.EVT_SELECTED_CHANGE);
    lens.addListener(function (data) { that.uiElements.overview.selectChange(data); }, SB_Lens.EVT_SELECTED_CHANGE);
    this.uiElements.overview.addListener(function (data) { lens.setSelectedObjectById(data); }, SB_UI_Overview.EVT_SELECTION);
    this.uiElements.overview.addListener(function (data)
    {
        builder.updateSceneFragmentMember_visibility(data.id, data.visible);
    }, SB_UI_Overview.EVT_VISIBILITY_TOGGLE);

    this.htmlElements.linkButton.onclick = function ()
    {
        that.uiElements.linkPanel.toggleVisibility();
        that.uiElements.linkPanel.buildUI();
    };
};




// Help Item configuration
SB_Controller.prototype.helpConfig =
{
    "camera":
    {
        items:
        {
            "F": "Reset camera to basic viewpoint",
            "R": "Reset camera to stored viewpoint",
        }
    },
    "ui":
    {
        items:
        {
            "B": "Show / hide the builder panel",
        }
    },
};




// Handle keyboard down events
SB_Controller.prototype.handleKeyDown = function (e, state)
{
    // B - Toggle visibility of builder panel
    if (e.keyCode == CGA_KeyCodes.KEY_B)
        this.uiElements.overview.toggleVisibility();

    // F - Set camera to simple default
    if (e.keyCode == CGA_KeyCodes.KEY_F)
        this.scene.gfxEngine.configureCameraBasic();
};




// Mouse has been clicked while hovering over an object
SB_Controller.prototype.handleObjectClicked = function(obj)
{
    this.lens.setSelectedObject(obj);
};




// Mouse has stopped hovering over an object
SB_Controller.prototype.handleObjectHovered = function(obj)
{
    this.lens.setHoveredObject(obj);
};

