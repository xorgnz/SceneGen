var DV_Persistor = function (communicator, scene, config, lens)
{
    // Store for later calls
    this.communicator = communicator;
    this.scene = scene;
    this.config = config;
    this.lens = lens;

    // State
    this.sceneId = null;
    this.sceneName = null;
    this.sceneFragmentId = null;
};



DV_Persistor.prototype.saveExistingScene = function (id, callback)
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




DV_Persistor.prototype.saveNewScene = function (callback)
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




DV_Persistor.prototype._addScene = function (callback)
{
    // Self reference
    var that = this;

    // Create name for new scene
    var d = new Date();
    var name = "Data Visualizer " +
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
                console.log("DV_Communicator.invoke_scene__add - Success. Scene ID: " + data.id);

                // Save state
                that.sceneId = data.id;
                that.sceneName = name;

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("DV_Communicator.invoke_scene__add - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("DV_Communicator.invoke_scene__add - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




DV_Persistor.prototype._addSceneFragment = function (callback)
{
    // Self reference
    var that = this;

    var sceneObjects = this.scene.gfxEngine.scene.getObjects();
    var simpleMembers = new Array();
    for (var i = 0 ; i < sceneObjects.length ; i ++)
    {
        var obj = sceneObjects[i];
        simpleMembers.push({
            entity:
            {
                id: obj.data.entity.id,
                name: obj.name,
            },
            style:
            {
                ambient: "#" + obj.mtl.ambient.getHexString(),
                diffuse: "#" + obj.mtl.color.getHexString(),
                emissive: "#" + obj.mtl.emissive.getHexString(),
                specular: "#" + obj.mtl.specular.getHexString(),
                shininess: obj.mtl.shininess,
                alpha: obj.mtl.opacity,
            },
            assetId: obj.data.asset.id,
        });
    }


    this.communicator
        .invoke_scene_fragment__add(
            this.sceneId,
            "Created in Data Visualizer",
            simpleMembers)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("DV_Communicator.invoke_scene_fragment__add_listType - Success. Scene Fragment ID: " + data.fragmentId);

                // Save state
                that.sceneFragmentId = data.fragmentId;

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("DV_Communicator.invoke_scene_fragment__add_listType - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("DV_Communicator.invoke_scene_fragment__add_listType - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




DV_Persistor.prototype._deleteSceneFragments = function (callback)
{
    this.communicator
        .invoke_scene_fragment__delete_byScene(this.sceneId)
        .done(function (data)
        {
            if (data.success)
            {
                console.log("DV_Communicator._deleteAllSceneFragments - Success.");

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("DV_Communicator._deleteAllSceneFragments - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("DV_Communicator._deleteAllSceneFragments - request failed: " + textStatus);
            console.log(errorThrown);
        });
};




DV_Persistor.prototype._saveViewpoint = function (callback)
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
                console.log("DV_Communicator.invoke_scene__update_viewpoint - Success. Scene ID: " + data.id);

                // Proceed to callback
                if (callback)
                    callback();
            }
            else
            {
                console.log("DV_Communicator.invoke_scene__update_viewpoint - Failure.");
                console.log(data.message);
            }
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("DV_Communicator.invoke_scene__update_viewpoint - request failed: " + textStatus);
            console.log(errorThrown);
        });
};

