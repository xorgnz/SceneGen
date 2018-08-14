/**************************************************************************
    Class - SceneBuilder That
**************************************************************************/
// Declare object
var SB_Communicator = function(config, scene)
{
    this.config = config;
};




SB_Communicator.prototype.invoke_asset__loadByAssetSet = function (id)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/asset/set/" + id + "/assets",
        dataType: 'json',
        type: 'get'
    });

};




SB_Communicator.prototype.invoke_asset_set__loadAll = function ()
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/asset/set/",
        dataType: 'json',
        type: 'get'
    });
};




SB_Communicator.prototype.invoke_query__loadAll = function ()
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/query/",
        dataType: 'json',
        type: 'get'
    });
};




SB_Communicator.prototype.invoke_scene__get = function (id)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/scene/" + id + "/full",
        dataType: 'json',
        data: {},
        type: 'get'
    });
};




SB_Communicator.prototype.invoke_scene__update = function (id, name)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/scene/" + id,
        dataType: 'json',
        data:
        {
            id: id,
            name: name,
        },
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene__update_viewpoint = function (id, viewpoint)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/scene/" + id + "/viewpoint",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({ viewpoint: viewpoint }),
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment__add_queryType = function (sceneId, name, assetSetId, stylesheetId, queryId, queryParams)
{
    return $.ajax({
        url: this.config.serverURL + "REST/sceneFragment/queryType",
        dataType: 'json',
        data:
        {
            sceneId: sceneId,
            name: name,
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
            queryId: queryId,
            queryParams: queryParams,
        },
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment__add_listType = function (sceneId, name, assetSetId, stylesheetId, assetIds)
{
    return $.ajax({
        url: this.config.serverURL + "REST/sceneFragment/listType/fromAssetIds",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(
        {
            sceneId: sceneId,
            name: name,
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
            assetIds: assetIds,
        }),
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment__delete = function (id)
{
    return $.ajax({
        url: this.config.serverURL + "REST/sceneFragment/" + id + "/delete",
        dataType: 'json',
        type: 'post'
    });

};




SB_Communicator.prototype.invoke_scene_fragment__update_listType = function (id, name, assetSetId, stylesheetId, assetIds)
{
    return $.ajax({
        url: this.config.serverURL + "REST/sceneFragment/" + id + "/listType/fromAssetIds",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(
        {
            name: name,
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
            assetIds: assetIds,
        }),
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment__update_queryType = function (id, name, assetSetId, stylesheetId, queryId, queryParams)
{
    return $.ajax({
        url: this.config.serverURL + "REST/sceneFragment/" + id + "/queryType",
        dataType: 'json',
        data:
        {
            name: name,
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
            queryId: queryId,
            queryParams: queryParams,
        },
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment_member__update_visibility = function (id, visible)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/sceneFragmentMember/" + id + "/visibility",
        dataType: 'json',
        data: {
            visible: visible,
        },
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_scene_fragment_member__update_material = function (id, mtl)
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/sceneFragmentMember/" + id + "/style",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(mtl),
        type: 'post'
    });
};




SB_Communicator.prototype.invoke_stylesheet__loadAll = function ()
{
    return $.ajax(
    {
        url: this.config.serverURL + "REST/stylesheet/",
        dataType: 'json',
        type: 'get'
    });
};
