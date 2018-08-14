/**************************************************************************
    Class - AE Entity Registry

    Manages map of fully loaded entities.

************************************************************************************/
var AE_EntityRegistry = function(main, historian)
{
    this.entityMap = {};
};




AE_EntityRegistry.prototype.getEntityById = function (id)
{
    return this.entityMap[id];
};




AE_EntityRegistry.prototype.registerEntities = function (entities)
{
    for (var i = 0 ; i < entities.length ; i++)
    {
        console.log("AE_EntityRegistry - registering " + entities[i].name);
        this.entityMap[entities[i].id] = entities[i];
    }
};
