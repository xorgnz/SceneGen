var AE_Persistor = function (communicator, scene, config, explorer, lens)
{
    // Store for later calls
    this.communicator = communicator;
    this.scene = scene;
    this.config = config;
    this.explorer = explorer;
    this.lens = lens;

    // State
    this.sceneId = null;
    this.sceneName = null;
    this.sceneFragmentId = null;
};




AE_Persistor.prototype.loadScene = function (id, callback)
{
    // Self reference
    var that = this;

    var scene = this.config.getScene(id);
    this.sceneId = id;
    this.sceneName = scene.name;

    // Remove existing entities
    this.scene.clearScene();

    // Define function chain
    var chain =
    [
        function (cb) { that._loadScene(cb); },
        function (cb) { if (callback) callback(); }
    ];

    callAsyncFunctionChain(chain);
};




AE_Persistor.prototype.saveExistingScene = function (id, callback)
{
    // Self reference
    var that = this;

    var scene = this.config.getScene(id);
    this.sceneId = id;
    this.sceneName = scene.name;

    // Define function chain
    var chain =
    [
        function (cb) { that._deleteSceneFragments(cb); },
        function (cb) { that._addSceneFragment(cb); },
        function (cb) { that._saveViewpoint(cb); },
        function (cb) { if (callback) callback(); }
    ];

    callAsyncFunctionChain(chain);
};




AE_Persistor.prototype.saveNewScene = function (callback)
{
    // Self reference
    var that = this;

    // Define function chain
    var chain =
    [
        function (cb) { that._addScene(cb); },
        function (cb) { that._addSceneFragment(cb); },
        function (cb) { that._saveViewpoint(cb); },
        function (cb) { if (callback) callback(); }
    ];

    callAsyncFunctionChain(chain);
};




AE_Persistor.prototype._addScene = function (callback)
{
    // Self reference
    var that = this;

    // Create name for new scene
    var d = new Date();
    var name = "Explorer " +
        d.getFullYear() + "-" +
        d.getMonth() + "-" +
        d.getDate() + " " +
        d.getHours() + ":" +
        d.getMinutes() + ":" +
        d.getSeconds() + "." +
        d.getMilliseconds();

    // Issue server API call
    this.communicator
        .invoke_scene__add(name)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("AE_Communicator.invoke_scene__add - Success. Scene ID: " + data.id);

                // Save state
                that.sceneId = data.id;
                that.sceneName = name;

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("AE_Communicator.invoke_scene__add - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_scene__add - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




AE_Persistor.prototype._addSceneFragment = function (callback)
{
    // Self reference
    var that = this;

    // Create list of entities in current scene
    var entityIds = this.scene.getEntityIds();

    this.communicator
        .invoke_scene_fragment__add_listType(
            this.sceneId,
            "Fragment from Explorer",
            this.config.assetSetId,
            this.config.stylesheetId,
            entityIds)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("AE_Communicator.invoke_scene_fragment__add_listType - Success. Scene ID: " + data.id);

                // Save state
                that.sceneFragmentId = data.fragmentId;

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("AE_Communicator.invoke_scene_fragment__add_listType - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_scene_fragment__add_listType - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




AE_Persistor.prototype._deleteSceneFragments = function (callback)
{
    this.communicator
        .invoke_scene_fragment__delete_byScene(this.sceneId)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("AE_Communicator._deleteAllSceneFragments - Success.");

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("AE_Communicator._deleteAllSceneFragments - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator._deleteAllSceneFragments - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




AE_Persistor.prototype._loadScene = function (callback)
{
    // Self reference
    var that = this;

    // Proceed
    this.communicator
        .invoke_scene__get(this.sceneId)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("AE_Communicator.invoke_scene__load - Success.");

                // Extract list of entity IDs from scene
                var entityIds = [];
                var fragments = data.descriptor.fragments;
                for (var i = 0 ; i < fragments.length ; i ++)
                    for (var j = 0 ; j < fragments[i].members.length ; j++)
                        entityIds.push(fragments[i].members[j].entity.id);

                // Instruct scene to add entities
                that.scene.addEntitiesById(entityIds, function ()
                {
                    console.log(data.descriptor);

                    // Configure camera with viewpoint
                    if (data.descriptor.viewpoint)
                    {
                        that.scene.gfxEngine.camera.setStoredConfiguration(data.descriptor.viewpoint);
                        that.scene.gfxEngine.camera.applyStoredConfiguration();
                    }

                    // Update lens
                    that.lens.repaintEntities(entityIds);

                    // Proceed to callback
                    if (callback)
                        callback();
                });
            }
            else
            {
                console.log("AE_Communicator.invoke_scene__load - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_scene__load - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




AE_Persistor.prototype._saveViewpoint = function (callback)
{
    // Retrieve current viewpoint
    var vp = this.scene.gfxEngine.camera.getCurrentConfiguration();

    // Issue save command
    this.communicator
        .invoke_scene__update_viewpoint(
            this.sceneId,
            vp)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("AE_Communicator.invoke_scene__update_viewpoint - Success. Scene ID: " + data.id);

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("AE_Communicator.invoke_scene__update_viewpoint - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_scene__update_viewpoint - request failed: " + textStatus);
            console.log(errorThrown);
        });
};

