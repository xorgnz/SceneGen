var AE_Main = function(config, cgaApp, startingEntityIds)
{
    // Self reference
    var that = this;

    // Create working structures
    this.cgaApp = cgaApp;
    this.config = config;
    this.scene = new AE_SceneManager(this.cgaApp.gfxEngine);
    this.communicator = new AE_Communicator(this.config.serverURL);
    this.controller = new AE_Controller(this);
    this.scene.setExplorer(this.controller.explorer);

    // Register components
    cgaApp.addController("ae", this.controller);

    this.cgaApp.gfxEngine.setCameraOffsets(-480,0);

    // Load configuration
    this.config.setCommunicator(this.communicator);
    this.config.loadAuxiliaries(function ()
    {
        // Update relation selction on focus panel
        that.controller.uiElements.commandPanel.buildUI();

        // Add scene contents
        that.scene.addEntitiesById(startingEntityIds, function ()
        {
            // Configure camera
            that.scene.gfxEngine.configureCameraBasic();

            // Update lens
            that.controller.lens.repaintEntities(startingEntityIds);
        });
    });
};
