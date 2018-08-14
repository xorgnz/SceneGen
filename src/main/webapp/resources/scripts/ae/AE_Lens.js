/**************************************************************************
    Class - AE Lens

    Keeps track of entity selection and view states . That is, selections, hovering, and focus
    Triggers changes to display of objects using scene manager

************************************************************************************/
var AE_Lens = function(scene, entityRegistry)
{
    // Associations
    this.scene = scene;
    this.entityRegistry = entityRegistry;

    // State variables
    this.centerFocus = null;     // Entity ID only
    this.focused = [];           // Entity ID only
    this.hovered = null;         // Entity ID only
    this.selected = null;        // Entity ID only
};




// Accessor methods
AE_Lens.prototype.getCenterFocusEntityId = function () { return this.centerFocus; };
AE_Lens.prototype.getHoveredEntityId     = function () { return this.hovered; };
AE_Lens.prototype.getSelectedEntityId    = function () { return this.selected; };
AE_Lens.prototype.isCenterFocus          = function (entityId) { return entityId == this.getCenterFocusEntityId(); };
AE_Lens.prototype.isHovered              = function (entityId) { return entityId == this.getHoveredEntityId(); };
AE_Lens.prototype.isSelected             = function (entityId) { return entityId == this.getSelectedEntityId(); };
AE_Lens.prototype.isInFocus              = function (entityId) { return this.focused[entityId] ? true : false; };




AE_Lens.prototype.repaintEntities = function (entityIds)
{
    // Paint entity states
    for (var i = 0 ; i < entityIds.length ; i++)
    {
        var id = entityIds[i];
        var state = AE_SceneManager.ES_HILIGHT_NONE;

        // Select entity state
        if      (id == this.selected && id == this.centerFocus) state = AE_SceneManager.ES_HILIGHT_SELECTED_CFOCUS;
        else if (id == this.centerFocus)                        state = AE_SceneManager.ES_HILIGHT_CFOCUS;
        else if (id == this.selected)                           state = AE_SceneManager.ES_HILIGHT_SELECTED;
        else if (id == this.hovered)                            state = AE_SceneManager.ES_HILIGHT_HOVERED;
        else if (_.contains(this.focused, id))                  state = AE_SceneManager.ES_HILIGHT_FOCUSED;

        // Apply to scene
        this.scene.setEntityStateSingle(id, state);
    }
};




AE_Lens.prototype.setFocusedEntities = function (centerEntityId, focusEntityIds)
{
    if (! focusEntityIds instanceof Array)
        throw "AE_Lens.setFocusedEntities - focusEntityIds not an array";

    // Gather IDs of dirty entities
    var entityIds = [];
    if (this.centerFocus != centerEntityId)
    {
        if (centerEntityId)   entityIds.push(centerEntityId);
        if (this.centerFocus) entityIds.push(this.centerFocus);
    }
    var xorFocusEntityIds = _.xor(this.focused, focusEntityIds);
    if (xorFocusEntityIds.length > 0)
        for (var i = 0 ; i < xorFocusEntityIds.length ; i++)
            entityIds.push(xorFocusEntityIds[i]);

    // Filter duplicates from list
    entityIds = _.uniq(entityIds);

    // Save new states
    this.centerFocus = centerEntityId;
    this.focused = focusEntityIds;

    // Repaint dirty entities
    this.repaintEntities(entityIds);
};




// Set entity with given ID as 'hovered'
// - entityId : ID of entity to highlight
AE_Lens.prototype.setHoveredEntity = function (entityId)
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
    }
};




// Set entity with given ID as 'selected'
// - entityId : ID of entity to highlight.
AE_Lens.prototype.setSelectedEntity = function (entityId)
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
    }
};

