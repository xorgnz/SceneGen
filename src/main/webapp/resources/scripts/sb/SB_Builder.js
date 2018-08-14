/**************************************************************************
    Class - SceneBuilder That
**************************************************************************/
// Declare object
var SB_Builder = function(config, communicator, scene)
{
    this.config = config;
    this.communicator = communicator;
    this.scene = scene;

    this.workState = false;

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "SB_Builder");
};
SB_Builder.LISTENER_WORK_STATE = 1;




// Add newly defined list based scene fragment to scene
//
// - Update scene representation on server
// - Reload scene descriptor and contents
SB_Builder.prototype.addListFragment = function (name, assetSetId, stylesheetId, assetIds)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Adding list fragment.");

    // Update scene fragment on server
    // - Trigger reload of full scene descriptor and contents
    this.communicator
        .invoke_scene_fragment__add_listType(this.scene.getId(), name, assetSetId, stylesheetId, assetIds)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Fragment Add failed: " + data.message;
        });
};




// Add newly defined query based scene fragment to scene
//
// - Update scene representation on server
// - Reload scene descriptor and contents
SB_Builder.prototype.addQueryFragment = function (name, assetSetId, stylesheetId, queryId, queryParams)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Adding query fragment.");

    // Update scene fragment on server
    // - Trigger reload of full scene descriptor and contents
    this.communicator
        .invoke_scene_fragment__add_queryType(this.scene.getId(), name, assetSetId, stylesheetId, queryId, queryParams)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Fragment Add failed: " + data.message;
        });
};




SB_Builder.prototype.deleteSceneFragment = function (id)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Deleting scene fragment.");

    // Update scene fragment on server
    // - Trigger reload of full scene descriptor and contents
    this.communicator
        .invoke_scene_fragment__delete(id)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Fragment Delete failed: " + data.message;
        });

};




// Load full contents of scene
SB_Builder.prototype.loadSceneContents = function ()
{
    console.log("Begin loading scene contents into SB Builder");

    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Loading scene content");

    this.communicator
        .invoke_scene__get(this.scene.getId())
        .done(function (data) {
            that.scene.update(data.descriptor, function () { that.setWorkState(false); });
        });
};




SB_Builder.prototype.setWorkState = function (state)
{
    // Log state change
    if (state)
        console.log("Builder entering state: " + state);
    else
        console.log("Builder leaving  state: " + this.workState);

    // Save state
    this.workState = state;

    // Notify listeners of state change
    this.fireEvent({workState: state}, SB_Builder.LISTENER_WORK_STATE);
};




// Update the scene
//
// - Update scene representation on server
// - Reload scene descriptor and contents
SB_Builder.prototype.updateScene = function (name)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Updating scene.");

    // Update scene on server
    // - Trigger reload of scene descriptor and contents
    this.communicator
        .invoke_scene__update(this.scene.getId(), name)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Update failed: " + data.message;
        });
};




SB_Builder.prototype.updateScene_viewpoint = function ()
{
    // Self reference
    var that = this;

    // Make current viewpoint default
    var vp = this.scene.gfxEngine.camera.getCurrentConfiguration();
    this.scene.gfxEngine.camera.setStoredConfiguration(vp);

    // Set work state
    this.setWorkState("Saving viewpoint");

    this.communicator
        .invoke_scene__update_viewpoint(this.scene.getId(), vp)
        .done(function (data) {
            that.setWorkState("Saving viewpoint - Success!");

            window.setTimeout(function () { that.setWorkState(false); }, 1000);
        });
};




// Update a list-based scene fragment
//
// - Update server representation
// - Reload scene descriptor and contents
SB_Builder.prototype.updateListFragment = function (fragment, name, assetSetId, stylesheetId, assetIds)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Updating list-based scene fragment.");

    // Update scene fragment on server
    // - Trigger reload of full scene descriptor and contents
    this.communicator
        .invoke_scene_fragment__update_listType(fragment.id, name, assetSetId, stylesheetId, assetIds)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Fragment Update failed: " + data.message;
        });
};




// Update a query-based scene fragment
//
// - Update server representation
// - Reload scene descriptor and contents
SB_Builder.prototype.updateQueryFragment = function (fragment, name, assetSetId, stylesheetId, queryId, queryParams)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Updating query-based scene fragment.");

    // Update scene fragment on server
    // - Trigger reload of full scene descriptor and contents
    this.communicator
        .invoke_scene_fragment__update_queryType(fragment.id, name, assetSetId, stylesheetId, queryId, queryParams)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.loadSceneContents();
            else
                throw "Scene Fragment Update failed: " + data.message;
        });
};




SB_Builder.prototype.updateSceneFragmentMember_material = function (object, mtl)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Adjusting scene object material");

    this.communicator
        .invoke_scene_fragment_member__update_material(object.id, mtl)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
            {
                object.setMaterial(mtl);
                object.setInitialMaterial(mtl);
            }
            else
                throw "Visibility toggle failed: " + data.message;
        });
};




SB_Builder.prototype.updateSceneFragmentMember_visibility = function (id, visible)
{
    // Self reference
    var that = this;

    // Set work state
    this.setWorkState("Adjusting scene object visibility");

    this.communicator
        .invoke_scene_fragment_member__update_visibility(id, visible)
        .done(function ( data ) {
            that.setWorkState(false);
            if (data.success)
                that.scene.setMemberVisibility(id, visible);
            else
                throw "Visibility toggle failed: " + data.message;
        });
};
