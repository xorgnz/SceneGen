/************************************************************************************
    Class - AE Scene Manager

    Manages the display of anatomy objects by the graphics engine. This primarily
    involves keeping track of current objects and all past explored objects, as
    well as providing methods to allow manipulation of these lists.

************************************************************************************/
var AE_SceneManager = function(gfxEngine)
{
    this.gfxEngine = gfxEngine;
    this.explorer = null; // To be set later
    this.settings = {};

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "AE_SceneManager");
};
AE_SceneManager.OPACITY_DEFAULT = 0.4;

// Events
AE_SceneManager.EVENT_UPDATED = "Scene Updated";

// Entity States
AE_SceneManager.ES_HILIGHT_CFOCUS          = 3;
AE_SceneManager.ES_HILIGHT_HOVERED         = 4;
AE_SceneManager.ES_HILIGHT_NONE            = 5;
AE_SceneManager.ES_HILIGHT_SELECTED        = 6;
AE_SceneManager.ES_HILIGHT_SELECTED_CFOCUS = 7;
AE_SceneManager.ES_HILIGHT_FOCUSED         = 8;

// Materials for marking objects
AE_SceneManager.MTL_CFOCUS          = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xa000a0, shininess: 80, specular: "#c0c0c0" };
AE_SceneManager.MTL_FOCUS           = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0x804080, shininess: 80, specular: "#c0c0c0" };
AE_SceneManager.MTL_HOVERED         = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xffff00, shininess: 80, specular: "#c0c0c0" };
AE_SceneManager.MTL_SELECTED        = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xff6000, shininess: 80, specular: "#c0c0c0" };
AE_SceneManager.MTL_SELECTED_CFOCUS = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xff4040, shininess: 80, specular: "#c0c0c0" };

AE_SceneManager.prototype.setExplorer = function (explorer) { this.explorer = explorer; };




// Create scene objects from descriptors and add to the scene. Fire callback on completion
//
// Newly added objects are shown using default opacity and provided materials
AE_SceneManager.prototype.addEntities = function (entities, callback)
{
    // Self reference
    var that = this;

    // Process entities
    var cgaObjectDescriptors = [];
    for (var i = 0 ; i < entities.length ; i++)
    {
        // Create CGA Object descriptor
        if (entities[i].filename)
        {
            console.log("AE_SceneManager.addEntities - Adding " + entities[i].name);

            cgaObjectDescriptors.push(new CGA_ObjectDescriptor(
                entities[i].id,
                entities[i].name,
                entities[i].filename,
                entities[i].style,
                true,
                entities[i]));
        }
        else
            console.log("AE_SceneManager.addEntities - Skipping " + entities[i].name + " - no model file available");
    }

    // Add object descriptors using graphics engine
    this.gfxEngine.addSceneObjects(cgaObjectDescriptors, function ()
    {
        // EVENT - Scene contents altered
        that.fireEvent(null, AE_SceneManager.EVENT_UPDATED);

        // Notify caller of completion
        callback();
    });
};




AE_SceneManager.prototype.addEntitiesById = function(entityIds, callback)
{
    // Self reference
    var that = this;

    // Load starting entities
    this.explorer.loadEntitiesFromId(entityIds, function ()
    {
        // Build list of entities from registry (both newly and previously loaded entities)
        var entities = [];
        for (var i = 0 ; i < entityIds.length ; i ++)
            entities.push(that.explorer.entityRegistry.getEntityById(entityIds[i]));

        // Load entities into scene
        that.addEntities(entities, function ()
        {
            if (callback)
                callback();
        });
    });
};




// Load renderable entities given a list of "partials", where a partial
// is an entity for whom only the ID and name are known.
AE_SceneManager.prototype.addEntitiesByPartial = function(partials, callback)
{
    // Self reference
    var that = this;

    // Load starting entities
    this.explorer.loadEntitiesFromPartial(partials, function ()
    {
        // Build list of entities from registry (both newly and previously loaded entities)
        var entities = [];
        for (var i = 0 ; i < partials.length ; i ++)
            entities.push(that.explorer.entityRegistry.getEntityById(partials[i].id));

        // Load entities into scene
        that.addEntities(entities, function ()
        {
            if (callback)
                callback();
        });
    });
};




AE_SceneManager.prototype.clearScene = function()
{
    this.removeEntities(this.getEntityIds());
};




// Determine whether an entity with this ID is in the scene
AE_SceneManager.prototype.containsEntity = function (entityId)
{
    return this.gfxEngine.getSceneObjectById(entityId) ? true : false;
};




AE_SceneManager.prototype.getEntityIds = function ()
{
    var entityIds = [];
    var objects = this.gfxEngine.scene.getObjects();
    for (var i = 0 ; i < objects.length ; i++)
        entityIds.push(objects[i].data.id);

    console.log("entity ids from scene");
    console.log(entityIds);

    return entityIds;
};




AE_SceneManager.prototype.removeEntity = function (entityId)
{
    var object = this.gfxEngine.getSceneObjectById(entityId);
    this.gfxEngine.scene.removeObject(object);

    // EVENT - Scene contents altered
    this.fireEvent(null, AE_SceneManager.EVENT_UPDATED);
};




AE_SceneManager.prototype.removeEntities = function (entityIds)
{
    for (var i = 0 ; i < entityIds.length ; i++)
    {
        var object = this.gfxEngine.getSceneObjectById(entityIds[i]);
        this.gfxEngine.scene.removeObject(object);
    }

    // EVENT - Scene contents altered
    this.fireEvent(null, AE_SceneManager.EVENT_UPDATED);
};




// Set an entity state on the entities indicated by the given array of IDs
// Note - it may be necessary to call repaint() to trigger any display changes
AE_SceneManager.prototype.setEntityStateSingle = function (id, state)
{
    // Get corresponding scene object
    var obj = this.gfxEngine.getSceneObjectById(id);

    if (obj)
    {
        switch (state)
        {
            case AE_SceneManager.ES_HILIGHT_CFOCUS:
                obj.setMaterial(AE_SceneManager.MTL_CFOCUS);
                break;

            case AE_SceneManager.ES_HILIGHT_HOVERED:
                obj.setMaterial(AE_SceneManager.MTL_HOVERED);
                break;

            case AE_SceneManager.ES_HILIGHT_NONE:
                obj.resetMaterial();
                break;

            case AE_SceneManager.ES_HILIGHT_SELECTED:
                obj.setMaterial(AE_SceneManager.MTL_SELECTED);
                break;

            case AE_SceneManager.ES_HILIGHT_SELECTED_CFOCUS:
                obj.setMaterial(AE_SceneManager.MTL_SELECTED_CFOCUS);
                break;

            case AE_SceneManager.ES_HILIGHT_FOCUSED:
                obj.setMaterial(AE_SceneManager.MTL_FOCUS);
                break;

            default:
                throw "State cannot be applied. Wrong plurality";
                break;
        }
    }
};
