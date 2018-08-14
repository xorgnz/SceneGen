var AE_Config = function (config)
{
    // Components
    this.communicator = null;

    // State variables
    this.stylesheetId = config.stylesheetId;
    this.assetSetId = config.assetSetId;
    this.serverURL = config.serverURL;

    this._modelsAvailable = [];
    this._relationMap = [];
    this._sceneMap = [];
};
AE_Config.prototype.addScene             = function (scene)    { this._sceneMap[scene.id] = scene; };
AE_Config.prototype.getRelations         = function ()         { return _.values(this._relationMap); };
AE_Config.prototype.getScene             = function (id)       { return this._sceneMap[id]; };
AE_Config.prototype.getScenes            = function ()         { return _.values(this._sceneMap); };
AE_Config.prototype.isModelAvailable     = function (id)       { return _.contains(this._modelsAvailable, id); };
AE_Config.prototype.setModelAvailability = function (modelIds) { this._modelsAvailable = _.union(this._modelsAvailable, modelIds); };




AE_Config.prototype.findRelation = function (relName)
{
    var relation = this._relationMap[relName];

    if (! relation)
        relation = { label: relName, relation: relName };

    return relation;
};




AE_Config.prototype.loadAuxiliaries = function (callback)
{
    // Self reference
    var that = this;

    var chain =
    [
        function (cb) { that.loadModelAvailability(cb); },
        function (cb) { that.loadRelations(cb); },
        function (cb) { that.loadScenes(cb); },
        function (cb) { if (callback) callback(); }
    ];

    callAsyncFunctionChain(chain);
};




AE_Config.prototype.loadModelAvailability = function (callback)
{
    console.log("AE_Config - Loading Model Availability data");

    // Self reference
    var that = this;

    // Initialize storage
    that._modelsAvailable = [];

    // Query server
    this.communicator
        .invoke_asset__loadByAssetSet(this.assetSetId)
        .done (function (data)
        {
            if (data.success)
            {
                console.log("AE_Config - Loading Model Availability data - done");

                for (var i = 0 ; i < data.assets.length ; i++)
                    that._modelsAvailable.push(data.assets[i].entityId);

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading model availability";
        });
};




AE_Config.prototype.loadRelations = function (callback)
{
    console.log("AE_Config - Loading relations");

    // Self reference
    var that = this;

    this.communicator
        .invoke_relation__load()
        .done( function (data)
        {
            if(data.success)
            {
                console.log("AE_Config - Loading relations - done");

                that._relationMap = _.indexBy(data.relations, "relation");

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading relations";
        });
};



AE_Config.prototype.loadScenes = function (callback)
{
    console.log("AE_Config - Loading scenes");

    // Self reference
    var that = this;

    this.communicator
        .invoke_scene__getAll()
        .done( function (data)
        {
            if(data.success)
            {
                console.log("AE_Config - Loading scenes - done");

                that._sceneMap = _.indexBy(data.scenes, "id");

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading scenes";
        });
};



AE_Config.prototype.setCommunicator = function (communicator)
{
    this.communicator = communicator;
};
