/**************************************************************************
    Class - SB Scene

    Manages the scene currently being edited.
    Works with the CGA_GraphicsEngine to display that scene.

**************************************************************************/
var SB_Scene = function (sceneId, gfxEngine)
{
    // Store references
    this.gfxEngine = gfxEngine;

    // Create default descriptor
    this.descriptor =
        {
            id: sceneId,
            name: "Loading..",
            stylesheet: { id: 0, name: "Unknown" },
            assetSet: { id: 0, name: "Unknown" },
            fragments: [],
        };

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "SB_Scene");
};
SB_Scene.prototype.getId         = function () { return this.descriptor.id; };
SB_Scene.prototype.getName       = function () { return this.descriptor.name; };
SB_Scene.prototype.getFragments  = function () { return this.descriptor.fragments; };
SB_Scene.prototype.setName       = function (val) { this.descriptor.name = val; };

// Object states
SB_Scene.OS_DEFAULT = 0;
SB_Scene.OS_HOVERED = 1;
SB_Scene.OS_SELECTED = 2;

// Highlight materials
SB_Scene.MTL_HOVERED         = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xffff00, shininess: 80, specular: "#c0c0c0" };
SB_Scene.MTL_SELECTED        = { ambient: "#c0c0c0", diffuse: "#c0c0c0", emissive: 0xff6000, shininess: 80, specular: "#c0c0c0" };




// Retrieve all scene object descriptors in fragments within the scene builder
// Ignore scene object descriptors marked 'missing'
//
// - Includes duplicate SFMs that refer to the same entity.
//
SB_Scene.prototype.getCurrentSceneMembers = function ()
{
    // Build map of members in current scene fragments
    var memberList = [];
    for (var i = 0 ; i < this.descriptor.fragments.length ; i ++)
    {
        var fragment = this.descriptor.fragments[i];

        for (var j = 0 ; j < fragment.members.length ; j ++)
        {
            var scnfmd = fragment.members[j];
            if (! scnfmd.missing)
                memberList.push(scnfmd);
        }
    }

    return memberList;
};




SB_Scene.prototype.getObjectById = function (entityId)
{
    return this.gfxEngine.getSceneObjectById(entityId);
};




SB_Scene.prototype.setMemberVisibility = function(id, visible)
{
    // Get GFX object
    var object = this.getObjectById(id);

    console.log("TODO - SB_Scene should have internal map for SFMs");

    // Find given member
    var m = false;
    if (this.descriptor && this.descriptor.fragments)
        for (var fkey in this.descriptor.fragments)
            for (var mkey in this.descriptor.fragments[fkey].members)
                if (this.descriptor.fragments[fkey].members[mkey].id == id)
                    m = this.descriptor.fragments[fkey].members[mkey];

    // Assert member visibility
    if (m)
        m.visible = visible;

    // Hide members showing same entity, if necessary
    if (visible)
        if (this.descriptor && this.descriptor.fragments)
            for (var fkey in this.descriptor.fragments)
                for (var mkey in this.descriptor.fragments[fkey].members)
                    if (this.descriptor.fragments[fkey].members[mkey].id != id &&
                        this.descriptor.fragments[fkey].members[mkey].entity.id == m.entity.id)
                    {
                        this.descriptor.fragments[fkey].members[mkey].visible = false;
                        this.getObjectById(this.descriptor.fragments[fkey].members[mkey].id).setVisibility(false);
                    }

    // Toggle object visiblity
    object.setVisibility(visible);

    // Fire event
    this.fireEvent(this.descriptor);
};




SB_Scene.prototype.setObjectState = function(object, state)
{
    if (object)
    {
        switch (state)
        {
        case SB_Scene.OS_DEFAULT:
            object.resetMaterial();
            break;

        case SB_Scene.OS_HOVERED:
            object.setMaterial(SB_Scene.MTL_HOVERED);
            break;

        case SB_Scene.OS_SELECTED:
            object.setMaterial(SB_Scene.MTL_SELECTED);
            break;

        default:
            break;
        }
    }
};




SB_Scene.prototype.update = function (descriptor, callback)
{
    console.log("Updating scene");
    console.log(descriptor);

    // Self reference
    var that = this;

    // Update descriptor
    this.descriptor = descriptor;
    var members = this.getCurrentSceneMembers();

    // Create CGA Object descriptors for all current scene members
    var cgaObjectDescriptors = [];
    for (var i = 0 ; i < members.length ; i ++)
        cgaObjectDescriptors[i] = new CGA_ObjectDescriptor(
            members[i].id,
            members[i].entity.name,
            members[i].asset.objFilename,
            members[i].style,
            members[i].visible,
            members[i]);

    // Replace Scene contents
    this.gfxEngine.replaceSceneObjects(cgaObjectDescriptors, function ()
    {
        // Use viewpoint if available
        if (descriptor.viewpoint)
        {
            var vp = descriptor.viewpoint;
            var cameraCfg = {};
            cameraCfg.position = new THREE.Vector3(vp.position.x, vp.position.y, vp.position.z);
            cameraCfg.rotation = new THREE.Vector3(vp.rotation.x, vp.rotation.y, vp.rotation.z);
            cameraCfg.target = new THREE.Vector3(vp.target.x, vp.target.y, vp.target.z);
            cameraCfg.up = new THREE.Vector3(vp.up.x, vp.up.y, vp.up.z);

            // Update camera
            that.gfxEngine.camera.setStoredConfiguration(cameraCfg);
            that.gfxEngine.camera.applyStoredConfiguration();
        }

        // Use basic camera if not
        else
        {
            that.gfxEngine.configureCameraBasic();
        }

        // Callback
        if (callback)
            callback();
    });

    // Notify listeners
    this.fireEvent(descriptor);
};



