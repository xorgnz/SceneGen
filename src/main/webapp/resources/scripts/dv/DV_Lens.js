/**************************************************************************
    Class - AE Lens

    Keeps track of entity selection and view states (selection & hovering)
    Triggers changes to display of objects using scene manager

************************************************************************************/
var DV_Lens = function(scene, entityRegistry)
{
    // Associations
    this.scene = scene;
    this.entityRegistry = entityRegistry;

    // State variables
    this.hovered = null;         // Entity ID only
    this.selected = null;        // Entity ID only

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "DV_Lens");
};
DV_Lens.LISTENER_HOVER_CHANGE = 2;
DV_Lens.LISTENER_SELECT_CHANGE = 3;




// Accessor methods
DV_Lens.prototype.getHoveredEntityId     = function () { return this.hovered; };
DV_Lens.prototype.getSelectedEntityId    = function () { return this.selected; };
DV_Lens.prototype.isHovered              = function (entityId) { return entityId == this.getHoveredEntityId(); };
DV_Lens.prototype.isSelected             = function (entityId) { return entityId == this.getSelectedEntityId(); };




DV_Lens.prototype.repaintEntities = function (entityIds)
{
    // Paint entity states
    for (var i = 0 ; i < entityIds.length ; i++)
    {
        var id = entityIds[i];
        var state = DV_SceneManager.ES_HILIGHT_NONE;

        // Select entity state
        if (id == this.selected)     state = DV_SceneManager.ES_HILIGHT_SELECTED;
        else if (id == this.hovered) state = DV_SceneManager.ES_HILIGHT_HOVERED;

        // Apply to scene
        this.scene.setEntityStateSingle(id, state);
    }
};




// Set entity with given ID as 'hovered'
// - entityId : ID of entity to highlight
DV_Lens.prototype.setHoveredEntity = function (entityId)
{
    if (this.hovered != entityId)
    {
        // Gather IDs of dirty entities
        var entityIds = [];
        if (entityId)     entityIds.push(entityId);
        if (this.hovered) entityIds.push(this.hovered);

        // Save new states
        this.hovered = entityId;

        // Repaint dirty entities
        this.repaintEntities(entityIds);

        // Fire listeners
        this.fireEvent(null, DV_Lens.LISTENER_HOVER_CHANGE);
    }
};




// Set entity with given ID as 'selected'
// - entityId : ID of entity to highlight.
DV_Lens.prototype.setSelectedEntity = function (entityId)
{
    if (this.selected != entityId)
    {
        // Gather IDs of dirty entities
        var entityIds = [];
        if (entityId)      entityIds.push(entityId);
        if (this.selected) entityIds.push(this.selected);

        // Save new states
        this.selected = entityId;

        // Repaint dirty entities
        this.repaintEntities(entityIds);

        // Fire listeners
        this.fireEvent(null, DV_Lens.LISTENER_SELECT_CHANGE);
    }
};

