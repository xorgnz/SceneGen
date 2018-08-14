/************************************************************************************
    Class - SB Lens

    Track hover and selection state for entities shown in scene.

    Note - this class relies on the assumption that a given entity will only ever
           shown once in a scene. This assumption is instantiated within SB_Scene
           in the way scenes are constructed from scene fragment descriptors.

           To remove this assumption, the setHovered and setSelected methods would
           need to rely on object IDs, not entity IDs

    Supports listeners:
        - on selection change (data passed is CGA_GeometryObject)
        - on hover change (data passed is CGA_GeometryObject)

************************************************************************************/
var SB_Lens = function(scene)
{
    // Store references
    this.scene = scene;

    // Create state variables
    this.hovered = null;
    this.selected = null;

    // Embellish as event source
    TEventDispatcher.embellish(this, "SB_Lens");
};
SB_Lens.EVT_HOVERED_CHANGE = 1;
SB_Lens.EVT_SELECTED_CHANGE = 2;




// Apply hover highlight
// - object : Object to highlight
SB_Lens.prototype.setHoveredObject = function (object)
{
    if (this.hovered != object)
    {
        // Clear old highlight
        if (this.hovered)
        {
            // Old object is selected
            // if      (this.hovered == this.selected)
               //  this.scene.setObjectState(this.hovered, SB_Scene.OS_SELECTED);

            // Old is neither
            // else
                this.scene.setObjectState(this.hovered, SB_Scene.OS_DEFAULT);
        }

        // Store object
        this.hovered = object;

        // Apply new highlight
        if (this.hovered)
            this.scene.setObjectState(this.hovered, SB_Scene.OS_HOVERED);

        // Fire events
        this.fireEvent({entity: this.hovered}, SB_Lens.EVT_HOVERED_CHANGE);
    }
};




// Apply hover highlight
// - object : Object to highlight
SB_Lens.prototype.setSelectedObject = function (object)
{
    if (this.selected != object)
    {
        // Clear old highlight
        if (this.selected)
        {
            // Old is hovered
            if      (this.selected == this.hovered)
                this.scene.setObjectState(this.selected, SB_Scene.OS_HOVERED);
            else
                this.scene.setObjectState(this.selected, SB_Scene.OS_DEFAULT);
        }

        // Store object
        this.selected = object;

        // Apply new highlight
        if (this.selected)
             this.scene.setObjectState(this.selected, SB_Scene.OS_SELECTED);

        // Fire events
        this.fireEvent({object: this.selected}, SB_Lens.EVT_SELECTED_CHANGE);
    }
};




SB_Lens.prototype.setSelectedObjectById = function(entityId)
{
    this.setSelectedObject(this.scene.getObjectById(entityId));
};





