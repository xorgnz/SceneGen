var DV_Visualizer = function (communicator, lens, sceneManager)
{
    this.communicator = communicator;
    this.lens = lens;
    this.sceneManager = sceneManager;

    // Data storage
    this._entityDataMap = {};
    this._loadedQueryId = null;
    this._loadedQueryParams = [];
    this._stylesheetId = null;
    this._sceneMembers = [];
    this._sceneViewpoint = null;

    // Flags
    this.flag_working = false;

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "AE_Explorer");
};

// Contants
DV_Visualizer.VISUALIZER_SCENE_CHANGE = 1;
DV_Visualizer.VISUALIZER_DATA_CHANGE = 2;
DV_Visualizer.VISUALIZER_STYLESHEET_CHANGE = 3;

// Accessors
DV_Visualizer.prototype.getDataByEntityId = function (entityId) { return this._entityDataMap[entityId]; };




DV_Visualizer.prototype.changeStylesheet = function (ssheetId, callback)
{
    // Self reference
    var that = this;

    // Abort if already working
    if (this.flag_working)
    {
        console.log("DV_Visualizer - already working. Ignoring request");
        return;
    }

    // Proceed
    else
    {
        console.log("DV_Visualizer.changeStylesheet - Starting - ID: " + ssheetId);

        // Raise flag
        this.flag_working = true;

        // Update state
        this._stylesheetId = ssheetId;

        // Compile stylables
        Promise.resolve()
            .then(function () { return that._applyStylesheet(); })
            .then(function () { that.sceneManager.clearScene(); })
            .then(function () { return that._addSceneObjects(); })
            .then(function ()
            {
                console.log("DV_Visualizer.changeStylesheet - Complete");

                // Lower flag
                that.flag_working = false;

                // Inform listeners
                that.fireEvent(null, DV_Visualizer.VISUALIZER_STYLESHEET_CHANGE);

                if (callback)
                    callback();
            })
            .then(undefined, function (error)
            {
                console.log("DV_Visualizer.changeStylesheet - Failed", error);

                // Lower flag
                that.flag_working = false;
            });
    }
};




DV_Visualizer.prototype.loadData = function (queryId, queryParams, callback)
{
    // Self reference
    var that = this;

    // Abort if already working
    if (this.flag_working)
    {
        console.log("DV_Visualizer - already working. Ignoring request");
        return;
    }

    // Proceed
    else
    {
        console.log("DV_Visualizer.loadData - Starting - ID: " + queryId);

        // Raise flag
        this.flag_working = true;

        // Retrieve list of entity IDs to request data for
        var entityIds = this.sceneManager.getEntityIds();

        // Issue data query request
        Promise.resolve(this.communicator.invoke_data__load(queryId, queryParams, entityIds))
            .then(function (response)
            {
                // Save received data
                for (var i = 0 ; i < response.entities.length ; i ++)
                {
                    var id = response.entities[i].id;
                    var data = response.entities[i].data;

                    // Create data object if none yet exists
                    if (! that._entityDataMap[id])
                        that._entityDataMap[id] = {};

                    // Copy data fields
                    for (var key in data)
                        that._entityDataMap[id][key] = data[key];
                }
            })
            .then(function () { return that._applyStylesheet(); })
            .then(function () { return that._addSceneObjects(); })
            .then(function ()
            {
                console.log("DV_Visualizer.loadData - Complete");

                // Lower flag
                that.flag_working = false;

                // Mark query as loaded
                that._loadedQueryId = queryId;
                that._loadedQueryParams = queryParams;

                // Inform listeners
                that.fireEvent(null, DV_Visualizer.VISUALIZER_DATA_CHANGE);

                if (callback)
                    callback();
            })
            .then(undefined, function (error)
            {
                console.log("DV_Visualizer.loadData - Failed", error);

                // Lower flag
                that.flag_working = false;
            });
    }
};




DV_Visualizer.prototype.loadScene = function (sceneId, callback)
{
    // Self reference
    var that = this;

    // Abort if already working
    if (this.flag_working)
    {
        console.log("DV_Visualizer - already working. Ignoring request");
        return;
    }

    // Proceed
    else
    {
        console.log("DV_Visualizer.loadScene - Starting - ID: " + sceneId);

        // Raise flag
        this.flag_working = true;

        // Define function chain
        Promise.resolve()
            .then(function () { return that._loadScene(sceneId); })
            .then(function () { return that._applyStylesheet(); })
            .then(function () { that.sceneManager.clearScene(); })
            .then(function () { return that._addSceneObjects(); })
            .then(function () { that.sceneManager.applyViewpoint(that._sceneViewpoint); })
            .then(function () { that.lens.repaintEntities(that.sceneManager.getEntityIds()); })
            .then(function ()
            {
                console.log("DV_Visualizer.loadScene - Complete");

                // Lower flag
                that.flag_working = false;

                // Inform listeners
                that.fireEvent(null, DV_Visualizer.VISUALIZER_SCENE_CHANGE);

                if (callback)
                    callback();
            })
            .then(undefined, function (error)
            {
                console.log("DV_Visualizer.loadScene - Failed", error);

                // Lower flag
                this.flag_working = false;
            });
    }
};




DV_Visualizer.prototype._addSceneObjects = function ()
{
    // Self reference
    var that = this;

    return new Promise(function (resolve, reject)
    {
        // Add scene members to scene
        that.sceneManager.addSceneMembers(that._sceneMembers, function ()
        {
            resolve();
        });
    });
};




DV_Visualizer.prototype._loadScene = function (sceneId)
{
    // Self reference
    var that = this;

    // Remove existing entities
    this.sceneManager.clearScene();

    return Promise
        .resolve(this.communicator.invoke_scene__get(sceneId))
        .then(function (response)
        {
            if (response.success)
            {
                // Extract members from scene descriptor
                var members = new Array();
                for (var i = 0 ; i < response.descriptor.fragments.length ; i ++)
                    for (var j = 0 ; j < response.descriptor.fragments[i].members.length ; j++)
                        members.push(response.descriptor.fragments[i].members[j]);

                // Save scene
                that._sceneMembers = members;
                that._sceneViewpoint = response.descriptor.viewpoint;
            }
        });
};




DV_Visualizer.prototype._applyStylesheet = function ()
{
    // Self reference
    var that = this;

    // Clear existing styles
    for (var i = 0 ; i < this._sceneMembers.length ; i ++)
        this._sceneMembers[i].style = null;

    if (this._stylesheetId)
    {
        // Compile stylables for each scene object
        var stylables = [];
        for (var i = 0 ; i < this._sceneMembers.length ; i++)
        {
            // Retrieve data for this.sceneManager object
            var data = this.getDataByEntityId(this._sceneMembers[i].entity.id);
            if (! data) data = {};

            stylables.push(
            {
                id: this._sceneMembers[i].entity.id,
                data: data,
                styleTags: this._sceneMembers[i].asset.styleTags,
            });
        };

        // Offer promise
        return Promise
            .resolve(this.communicator.invoke_stylesheet__apply(this._stylesheetId, stylables))
            .then(function (response)
            {
                for (var i = 0 ; i < that._sceneMembers.length ; i++)
                    that._sceneMembers[i].style = response.styleMap[that._sceneMembers[i].entity.id];
            });
    }
};
