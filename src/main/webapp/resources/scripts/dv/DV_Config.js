var DV_Config = function (config)
{
    // Components
    this.communicator = null;

    // State variables
    this.serverURL = config.serverURL;

    this._sceneMap = [];
    this._queryMap = [];
    this._stylesheetMap = [];
};
DV_Config.prototype.addScene             = function (scene)    { this._sceneMap[scene.id] = scene; };
DV_Config.prototype.getQueries           = function ()         { return _.values(this._queryMap); };
DV_Config.prototype.getScene             = function (id)       { return this._sceneMap[id]; };
DV_Config.prototype.getScenes            = function ()         { return _.values(this._sceneMap); };
DV_Config.prototype.getStylesheets       = function ()         { return _.values(this._stylesheetMap); };




DV_Config.prototype.loadAuxiliaries = function (callback)
{
    // Self reference
    var that = this;

    var chain =
    [
        function (cb) { that._loadScenes(cb); },
        function (cb) { that._loadQueries(cb); },
        function (cb) { that._loadStylesheets(cb); },
        function (cb) { if (callback) callback(); }
    ];

    callAsyncFunctionChain(chain);
};




DV_Config.prototype.setCommunicator = function (communicator)
{
    this.communicator = communicator;
};




DV_Config.prototype._loadQueries = function (callback)
{
    console.log("DV_Config - Loading queries");

    // Self reference
    var that = this;

    this.communicator
        .invoke_query__getAll()
        .done( function (data)
        {
            if(data.success)
            {
                console.log("DV_Config - Loading queries - done");

                // Filter queries to list only data queries
                var queries = [];
                for (var i = 0 ; i < data.queries.length ; i++)
                    if (data.queries[i].tags.indexOf("data") != -1)
                        queries.push(data.queries[i]);

                that._queryMap = _.indexBy(queries, "id");

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading queries";
        });
};




DV_Config.prototype._loadScenes = function (callback)
{
    console.log("DV_Config - Loading scenes");

    // Self reference
    var that = this;

    this.communicator
        .invoke_scene__getAll()
        .done( function (data)
        {
            if(data.success)
            {
                console.log("DV_Config - Loading scenes - done");

                that._sceneMap = _.indexBy(data.scenes, "id");

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading scenes";
        });
};




DV_Config.prototype._loadStylesheets = function (callback)
{
    console.log("DV_Config - Loading stylesheets");

    // Self reference
    var that = this;

    this.communicator
        .invoke_stylesheet__getAll()
        .done( function (data)
        {
            if(data.success)
            {
                console.log("DV_Config - Loading stylesheets - done");

                that._stylesheetMap = _.indexBy(data.stylesheets, "id");

                if (callback)
                    callback();
            }
            else
                throw "Failure while loading stylesheets";
        });
};
