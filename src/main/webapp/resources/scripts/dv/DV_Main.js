var DV_Main = function(config, cgaApp)
{
    // Self reference
    var that = this;

    // Create working structures
    this.cgaApp = cgaApp;
    this.config = config;
    this.sceneManager = new DV_SceneManager(this.cgaApp.gfxEngine);
    this.communicator = new DV_Communicator(this.config.serverURL);
    this.controller = new DV_Controller(this);

    // Register components
    cgaApp.addController("dv", this.controller);

    this.cgaApp.gfxEngine.setCameraOffsets(-480,0);

    // Load configuration
    this.config.setCommunicator(this.communicator);
    this.config.loadAuxiliaries(function ()
    {
        // Update relation selction on focus panel
        that.controller.uiElements.commandPanel.buildUI();

        // Configure camera
        that.sceneManager.gfxEngine.configureCameraBasic();
    });
};
