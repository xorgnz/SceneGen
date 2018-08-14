var SB_Main = function(config, cgaApp)
{
    // Create working structures
    this.cgaApp = cgaApp;
    this.config = config;

    // Create components
    this.scene = new SB_Scene(config.sceneId, cgaApp.gfxEngine);
    this.communicator = new SB_Communicator(this.config);
    this.builder = new SB_Builder(this.config, this.communicator, this.scene);
    this.lens = new SB_Lens(this.scene);

    // Register controllers
    this.controller = new SB_Controller(this, this.scene, this.lens, this.builder);
    cgaApp.addController("sb", this.controller);

    // Re-center scene to avoid UI elements
    this.cgaApp.gfxEngine.setCameraOffsets(-400,0);

    // Load auxiliary objects into config
    this.config.loadAuxiliaries(this.communicator);

    // Wait for config to finish loading, then load scene contents
    this.startAfterConfigLoad();
};




SB_Main.prototype.startAfterConfigLoad = function ()
{
    // Self reference
    var that = this;

    // Auxiliaries are loaded, load scene contents
    if (this.config.isLoadComplete())
        this.builder.loadSceneContents();

    // Auxiliaries not yet loaded - try again in 200 ms
    else
        window.setTimeout(function () { that.startAfterConfigLoad(); }, 200);
};
