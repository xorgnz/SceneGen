/************************************************************************************
    Class - AE Communicator

    Dispatch and handle requests to server to anatomy exploration.
    - Uses callbacks to notify completion of requests
    - Uses main config for auxiliary parameters (URLs, stylesheet & asset set IDs)

************************************************************************************/
var AE_Communicator = function(serverURL, relationManager)
{
    this.serverURL = serverURL;
};




// Return data includes:
// - List<Asset> assets
AE_Communicator.prototype.invoke_asset__loadByAssetSet = function (id)
{
    console.log("AE_Communicator.asset__loadByAssetSet. Asset Set ID: " + id);

    return $.ajax(
    {
        url: this.serverURL + "REST/asset/set/" + id + "/assets",
        dataType: 'json',
        type: 'get'
    });
};




// Return data includes:
// - RenderableEntityDescriptor entity
AE_Communicator.prototype.invoke_entity__load = function (entityId, assetSetId, stylesheetId)
{
    console.log("AE_Communicator.entity__load. Stylesheet ID " + stylesheetId + ", Asset Set ID " + assetSetId + ", ID: " + entityId);

    // Send request
    return $.ajax({
        url: this.serverURL + "/REST/explorer/entity/" + entityId,
        dataType: 'json',
        data: {
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
        },
        type: 'get'
    });
};




// Return data includes:
// - RenderableEntityDescriptor entity
AE_Communicator.prototype.invoke_entity__loadByName = function (name, assetSetId, stylesheetId)
{
    console.log("AE_Communicator.entity__loadByName " + name);

    // Send request
    return $.ajax({
        url: this.serverURL + "REST/explorer/entity/byName/" + name,
        dataType: 'json',
        data: {
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
        },
        type: 'get'
    });
};




// Retrieve all entities related to a given entity by a given relationship
// Notify caller when done
// - entityId : Target entity
// - relation : Target relation
//
// Return data includes:
// - List<RenderableEntityDescriptor> entities
// AE_Communicator.prototype.invoke_entity__loadByRelationship = function (entityId, relation, assetSetId, stylesheetId)
// {
//     console.log("AE_Communicator.invoke_entity__loadByRelationship. Entity: " + entityId + ", relation: " + relation);
//
//     // Send request
//     return $.ajax({
//         url: this.serverURL + "/explorer/entities/byRelation",
//         dataType: 'json',
//         data:
//         {
//             assetSetId: assetSetId,
//             relation: relation,
//             stylesheetId: stylesheetId,
//             entityId: entityId
//         },
//         type: 'get'
//     });
// };




// Return data includes:
// - List<RenderableEntityDescriptor> entities
AE_Communicator.prototype.invoke_entity__loadSeveral = function (stylesheetId, assetSetId, entityIds)
{
    console.log("AE_Communicator.entity__loadSeveral. Stylesheet ID " + stylesheetId + ", Asset Set ID " + assetSetId, ", Entities: ");
    console.log(entityIds);

    // Send request
    return $.ajax({
        url: this.serverURL + "/REST/explorer/entity/several",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            stylesheetId: stylesheetId,
            assetSetId: assetSetId,
            entityIds: entityIds
        }),
        type: 'post'
    });
};




// Transform partial entities in renderable entities
//
// Return data includes:
// - List<RenderableEntityDescriptor> entities
AE_Communicator.prototype.invoke_entity__fromPartials = function (stylesheetId, assetSetId, partials)
{
    console.log("AE_Communicator.entity__loadSeveral. Stylesheet ID " + stylesheetId + ", Asset Set ID " + assetSetId + ", Entities: ");
    console.log(partials);

    // Send request
    return $.ajax({
        url: this.serverURL + "/REST/explorer/entity/fromPartials",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            stylesheetId: stylesheetId,
            assetSetId: assetSetId,
            partials: partials
        }),
        type: 'post'
    });
};




// Return data includes:
// - List<Relation> relations
AE_Communicator.prototype.invoke_relation__load = function ()
{
    console.log("AE_Communicator.relation__load");

    return $.ajax({
        url: this.serverURL + "REST/explorer/relation",
        dataType: 'json',
        type: 'get'
    });
};




// Retrieve list of all relations and related entities for a given entity
// - entityId : ID of entity to retrieve relations for
//
// Return data include:
// - List<Relationship> relationships
AE_Communicator.prototype.invoke_relationship__loadByEntity = function (entityId)
{
    console.log("AE_Communicator.invoke_relationship__loadByEntity " + entityId);

    // Send request
    return $.ajax({
        url: this.serverURL + "/REST/explorer/exploreEntity/" + entityId,
        dataType: 'json',
        type: 'get'
    });
};




// Retrieve list of all relationships of a given relation that descend from a given entity
//
// Return data includes:
// - List<Relationship> relationships
AE_Communicator.prototype.invoke_relationship__loadByEntityRelationCascade = function (entityId, relation)
{
    console.log("AE_Communicator.invoke_relationship__loadByEntityRelationCascade " + entityId + ", " + relation);

    // Send request
    return $.ajax({
        url: this.serverURL + "/REST/explorer/relationships/byEntityRelation/cascade",
        dataType: 'json',
        data:
        {
            relation: relation,
            entityId: entityId
        },
        type: 'get'
    });
};




// Create a scene with the given name
AE_Communicator.prototype.invoke_scene__add = function (name)
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




AE_Communicator.prototype.invoke_scene__get = function (id)
{
    console.log("AE_Communicator.invoke_scene__getAll");

    return $.ajax(
    {
        url: this.serverURL + "REST/scene/" + id + "/full",
        dataType: 'json',
        data: {},
        type: 'get'
    });
};




AE_Communicator.prototype.invoke_scene__getAll = function ()
{
    console.log("AE_Communicator.invoke_scene__getAll");

    return $.ajax({
        url: this.serverURL + "REST/scene",
        dataType: 'json',
        type: 'get'
    });
};



AE_Communicator.prototype.invoke_scene__update_viewpoint = function (id, viewpoint)
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




AE_Communicator.prototype.invoke_scene_fragment__add_listType = function (sceneId, name, assetSetId, stylesheetId, entityIds)
{
    return $.ajax({
        url: this.serverURL + "REST/sceneFragment/listType/fromEntityIds",
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(
        {
            sceneId: sceneId,
            name: name,
            assetSetId: assetSetId,
            stylesheetId: stylesheetId,
            entityIds: entityIds,
        }),
        type: 'post'
    });
};




AE_Communicator.prototype.invoke_scene_fragment__delete_byScene = function (sceneId)
{
    return $.ajax({
        url: this.serverURL + "REST/scene/" + sceneId + "/deleteFragments",
        dataType: 'json',
        type: 'post'
    });
};
