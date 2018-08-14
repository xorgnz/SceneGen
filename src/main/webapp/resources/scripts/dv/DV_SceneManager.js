/************************************************************************************
    Class - AE Scene Manager

    Manages the display of anatomy objects by the graphics engine. This primarily
    involves keeping track of current objects, as well as providing methods to allow
    manipulation of these lists.

************************************************************************************/
var DV_SceneManager = function(gfxEngine)
{
    this.gfxEngine = gfxEngine;
    this.settings = {};

    this._entityId_sceneObjectMap = {};
};
DV_SceneManager.OPACITY_DEFAULT = 0.4;

// Entity States
DV_SceneManager.ES_HILIGHT_HOVERED         = 4;
DV_SceneManager.ES_HILIGHT_NONE            = 5;
DV_SceneManager.ES_HILIGHT_SELECTED        = 6;

// Materials for marking objects
DV_SceneManager.MTL_HOVERED         = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xffff00, shininess: 80, specular: "#c0c0c0" };
DV_SceneManager.MTL_SELECTED        = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xff6000, shininess: 80, specular: "#c0c0c0" };

// Accessors
DV_SceneManager.prototype.getObjectById        = function (id) { return this.gfxEngine.getSceneObjectById(id); };
DV_SceneManager.prototype.getObjectByEntityId  = function (id) { return this._entityId_sceneObjectMap[id]; };




DV_SceneManager.prototype.addSceneMembers = function (members, callback)
{
    // Self reference
    var that = this;

    // Compile CGA Object descriptors from scene members
    var cgaObjectDescriptors = [];
    for (var j = 0 ; j < members.length ; j++)
    {
        var member = members[j];

        if (! member.missing)
            cgaObjectDescriptors.push(new CGA_ObjectDescriptor(
                member.id,
                member.entity.name,
                member.asset.objFilename,
                member.style,
                member.visible,
                member));
    }

    // Replace Scene contents
    this.gfxEngine.replaceSceneObjects(cgaObjectDescriptors, function ()
    {
        // Update entity ID -> scene object map
        that._updateEntityIdSceneObjectMap();

        // Callback
        if (callback)
            callback();
    });
};




DV_SceneManager.prototype.applyViewpoint = function (viewpoint)
{
    if (viewpoint)
    {
        this.gfxEngine.camera.setStoredConfiguration(viewpoint);
        this.gfxEngine.camera.applyStoredConfiguration();
    }
    else
    {
        this.gfxEngine.configureCameraBasic();
    }
};




DV_SceneManager.prototype.clearScene = function ()
{
    var entityIds = this.getEntityIds();

    for (var i = 0 ; i < entityIds.length ; i++)
    {
        var object = this.gfxEngine.getSceneObjectById(entityIds[i]);
        this.gfxEngine.scene.removeObject(object);
    }
};




DV_SceneManager.prototype.getEntityIds = function ()
{
    var entityIds = [];
    var objects = this.gfxEngine.scene.getObjects();
    for (var i = 0 ; i < objects.length ; i++)
        entityIds.push(objects[i].data.entity.id);

    return entityIds;
};




DV_SceneManager.prototype.getObjectData = function ()
{
    var data = [];
    var objects = this.gfxEngine.scene.getObjects();
    for (var i = 0 ; i < objects.length ; i++)
        data.push(objects[i].data);

    return data;
};




// Set an entity state on the entities indicated by the given array of IDs
// Note - it may be necessary to call repaint() to trigger any display changes
DV_SceneManager.prototype.setEntityStateSingle = function (id, state)
{
    // Get corresponding scene object
    var obj = this.gfxEngine.getSceneObjectById(id);

    if (obj)
    {
        switch (state)
        {
            case DV_SceneManager.ES_HILIGHT_HOVERED:
                obj.setMaterial(DV_SceneManager.MTL_HOVERED);
                break;

            case DV_SceneManager.ES_HILIGHT_NONE:
                obj.resetMaterial();
                break;

            case DV_SceneManager.ES_HILIGHT_SELECTED:
                obj.setMaterial(DV_SceneManager.MTL_SELECTED);
                break;

            default:
                throw "State cannot be applied - unknown";
                break;
        }
    }
};




DV_SceneManager.prototype._updateEntityIdSceneObjectMap = function ()
{
    this._entityId_sceneObjectMap = {};

    var objects = this.gfxEngine.scene.getObjects();
    for (var i = 0 ; i < objects.length ; i++)
        this._entityId_sceneObjectMap[objects[i].data.entity.id] = objects[i];
};
