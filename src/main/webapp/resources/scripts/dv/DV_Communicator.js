/************************************************************************************
    Class - AE Communicator

    Dispatch and handle requests to server to anatomy exploration.
    - Uses callbacks to notify completion of requests
    - Uses main config for auxiliary parameters (URLs, stylesheet & asset set IDs)

************************************************************************************/
var DV_Communicator = function(serverURL, relationManager)
{
    this.serverURL = serverURL;
};




// Create a scene with the given name
DV_Communicator.prototype.invoke_data__load = function (queryId, queryParams, entityIds)
{
    return $.ajax({
        url: this.serverURL + "REST/visualizer/dataQuery",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            queryId: queryId,
            queryParams: queryParams,
            entityIds: entityIds,
        }),
        type: 'post'
    });
};




DV_Communicator.prototype.invoke_query__getAll = function ()
{
    return $.ajax(
    {
        url: this.serverURL + "REST/query/",
        dataType: 'json',
        type: 'get'
    });
};




// Create a scene with the given name
DV_Communicator.prototype.invoke_scene__add = function (name)
{
    return $.ajax({
        url: this.serverURL + "REST/scene/add",
        dataType: 'json',
        data:
        {
            name: name,
        },
        type: 'post'
    });
};




DV_Communicator.prototype.invoke_scene__get = function (id)
{
    console.log("DV_Communicator.invoke_scene__getAll");

    return $.ajax(
    {
        url: this.serverURL + "REST/scene/" + id + "/full",
        dataType: 'json',
        data: {},
        type: 'get'
    });
};




DV_Communicator.prototype.invoke_scene__getAll = function ()
{
    console.log("DV_Communicator.invoke_scene__getAll");

    return $.ajax({
        url: this.serverURL + "REST/scene",
        dataType: 'json',
        type: 'get'
    });
};




DV_Communicator.prototype.invoke_scene__update_viewpoint = function (id, viewpoint)
{
    return $.ajax(
    {
        url: this.serverURL + "REST/scene/" + id + "/viewpoint",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({ viewpoint: viewpoint }),
        type: 'post'
    });
};




DV_Communicator.prototype.invoke_scene_fragment__add = function (sceneId, name, members)
{
    return $.ajax({
        url: this.serverURL + "REST/sceneFragment/listType/fromMembers",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(
        {
            sceneId: sceneId,
            name: name,
            members: members,
        }),
        type: 'post'
    });
};




DV_Communicator.prototype.invoke_scene_fragment__delete_byScene = function (sceneId)
{
    return $.ajax({
        url: this.serverURL + "REST/scene/" + sceneId + "/deleteFragments",
        dataType: 'json',
        type: 'post'
    });
};




DV_Communicator.prototype.invoke_stylesheet__getAll = function ()
{
    return $.ajax(
    {
        url: this.serverURL + "REST/stylesheet/",
        dataType: 'json',
        type: 'get'
    });
};




DV_Communicator.prototype.invoke_stylesheet__apply = function (stylesheetId, stylables)
{
    return $.ajax(
    {
        url: this.serverURL + "REST/visualizer/applyStylesheet",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            stylesheetId: stylesheetId,
            stylables: stylables,
        }),
        type: 'post'
    });
};
