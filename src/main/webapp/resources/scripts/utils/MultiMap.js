var MultiMap = function (identifier)
{
    // Save map name
    identifier = identifier || "Unnamed";
    this.identifier = identifier;

    // Initialize storage
    this.items = {};
};


MultiMap.prototype.put = function (key, item)
{
    key = key || item;

    if (this.items[key] && this.items[key] instanceof Array)
        this.items[key].push(item);
    else
        this.items[key] = [ item ];
};


MultiMap.prototype.keys = function ()
{
    var response = [];
    for (var key in this.items)
        response.push(key);
};


MultiMap.prototype.get = function (key)
{
    var list = this.items[key];

    if (list && list instanceof Array)
    {
        var response = [];
        for (var i = 0 ; i < list.length ; i ++)
            response[i] = list[i];

        return response;
    }
    else
    {
        console.log("Cannot get " + key + "from MultiMap '" + this.identifier + "'. Null entry");
        return [];
    }
};


MultiMap.prototype.getAll = function ()
{
    var response = [];
    for (var key in this.items)
    {
        if (this.items[key] && this.items[key] instanceof Array)
        {
            response[key] = [];
            for (var i = 0 ; i < this.items[key].length ; i ++)
                response[key][i] = this.items[key][i];
        }
    }

    return response;
};


MultiMap.prototype.has = function (key)
{
    return this.items[key] && this.items[key] instanceof Array;
};


MultiMap.prototype.remove = function (key)
{
    this.items[key] = null;
};