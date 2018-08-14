var SB_Config = function(config)
{
    // Prepare config storage
    this.sceneId = config.sceneId;
    this.serverURL = config.serverURL;
    this.stylesheets = [];
    this.queries = [];
    this.assetSets = [];

    // Set flags
    this.load_assetSets_complete = false;
    this.load_queries_complete = false;
    this.load_stylesheets_complete = false;
};



SB_Config.prototype.getAssetSet = function(id)
{
    for (var i = 0 ; i < this.assetSets.length ; i ++)
    {
        if (this.assetSets[i].id == id)
            return this.assetSets[i];
    }

    throw "Unknown asset set requested from config (" + id + ")";
};




// Retrieve the name of the query with the given ID, if it exists in the SB config
SB_Config.prototype.getQuery = function(id)
{
    for (var i = 0 ; i < this.queries.length ; i ++)
    {
        if (this.queries[i].id == id)
            return this.queries[i];
    }

    throw "Unknown query requested from config (" + id + ")";
};




// Retrieve the name of the stylesheet with the given ID, if it exists in the SB config
SB_Config.prototype.getStylesheet = function(id)
{
    for (var i = 0 ; i < this.stylesheets.length ; i ++)
    {
        if (this.stylesheets[i].id == id)
            return this.stylesheets[i];
    }

    throw "Unknown stylesheet requested from config (" + id + ")";
};




SB_Config.prototype.isLoadComplete = function()
{
    return this.load_stylesheets_complete && this.load_queries_complete && this.load_assetSets_complete;
};




SB_Config.prototype.loadAuxiliaries = function (communicator)
{
    console.log("Begin loading auxiliaries into SB Config");

    // Self reference
    var that = this;

    // Set flags
    this.load_assetSets_complete = false;
    this.load_queries_complete = false;
    this.load_stylesheets_complete = false;

    // Start loading
    communicator
        .invoke_asset_set__loadAll()
        .done(function ( data ) {
            if (data.success)
            {
                console.log("Config - Asset Sets loaded");
                console.log(data.assetSets);

                that.assetSets = data.assetSets;
                that.load_assetSets_complete = true;
            }
            else
                throw "Fatal error - could not load asset sets";
        });
    communicator
        .invoke_query__loadAll()
        .done(function ( data ) {
            if (data.success)
            {
                console.log("Config - Queries loaded");
                console.log(data.queries);

                // Filter queries to list only entity queries
                that.queries = [];
                for (var i = 0 ; i < data.queries.length ; i++)
                    if (data.queries[i].tags.indexOf("entities") != -1)
                        that.queries.push(data.queries[i]);

                that.load_queries_complete = true;
            }
            else
                throw "Fatal error - could not load asset sets";
        });
    communicator
        .invoke_stylesheet__loadAll()
        .done(function ( data ) {
            if (data.success)
            {
                console.log("Config - Stylesheets loaded");
                console.log(data.stylesheets);

                that.stylesheets = [];
                for (var i = 0 ; i < data.stylesheets.length ; i++)
                {
                    console.log(data.stylesheets[i].tags);
                    if (data.stylesheets[i].tags.indexOf("data") == -1)
                        that.stylesheets.push(data.stylesheets[i]);
                }
                that.load_stylesheets_complete = true;
            }
            else
                throw "Fatal error - could not load asset sets";
        });
};
