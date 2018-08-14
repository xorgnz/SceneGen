var AE_DrillPanel = function (container, controller)
{
    this.container = container;
    this.explorer = controller.explorer;    // Exploration commands
    this.scene = controller.main.scene;     // Info on entities in scene
    this.config = controller.main.config;   // Info on model availability; Scene mod commands
    this.lens = controller.lens;            // Entity paint commands

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_DrillPanel");

    this.entity = null;
    this.relName = null;
    this.relationships = null;
};




AE_DrillPanel.prototype.consumeEvent = function (data, type)
{
    // Work - Exploration changed
    if (type == AE_Explorer.EVENT_EXP_CHANGED)
        $(this.container).hide();

    // Work - Relation changed
    else if (type == AE_Explorer.EVENT_DRILL_CHANGED)
    {
        if (data.relName)
        {
            this.entity = data.entity;
            this.relName = data.relName;
            this.relationships = data.relationships;

            this._buildDrill(data.entity, data.relName, data.relationships);
            $(this.container).show();
        }
        else
            $(this.container).hide();
    }

    // EVENT - Scene changed
    else if (type == AE_SceneManager.EVENT_UPDATED)
    {
        if ($(this.container).is(':visible'))
            this._buildDrill(this.entity, this.relName, this.relationships);
    }
};




AE_DrillPanel.prototype._buildDrill = function (entity, relName, relationships)
{
    // Self reference
    var that = this;

    // Erase previous content
    this.container.innerHTML = "";

    // If no relationships loaded, and exploration in progress
    if (!relationships && this.explorer.flag_drilling)
        this.container.appendChild(DOMUtils.div("", "", "Waiting for relationships to load..", "font-weight:bold; padding-left: 4px;"));

    // Relationship list
    else
    {
        // Create multimap
        var relMap = new MultiMap();
        for (var i = 0 ; i < relationships.length ; i++)
            relMap.put(relationships[i].subject.id, relationships[i]);

        // Component - Header
        var div_header_head = DOMUtils.header(1, entity.name);
        var div_header_text = DOMUtils.div("text", "");
        div_header_text.appendChild(DOMUtils.text("Drilling into ", true, false));
        div_header_text.appendChild(DOMUtils.text(relName, false, false));

        // Component - "Add" button
        var div_add_button = DOMUtils.div("button", null, "Add");
        div_add_button.relationships = relationships;
        div_add_button.onclick = function () {
            that._evt_addRelationshipsToScene(this.relationships);
        };

        // Component - "Remove" button
        var div_remove_button = DOMUtils.div("button", null, "Remove");
        div_remove_button.relationships = relationships;
        div_remove_button.onclick = function () {
            that._evt_removeEntitiesFromSceneGivenRelationships(this.relationships);
        };

        // Assemble - Header
        var div_header = DOMUtils.div("block_header");
        div_header.appendChild(div_remove_button);
        div_header.appendChild(div_add_button);
        div_header.appendChild(div_header_head);
        div_header.appendChild(div_header_text);

        // Component - Drill results
        var div_drillResults = DOMUtils.div("drillResults");
        if (relationships.length == 0)
            div_drillResults.appendChild(DOMUtils.div("","", "No relationships available", "margin: 8px 0px 0px 3px;"));
        else
            div_drillResults.appendChild(this._buildDrill_recurse(entity.id, entity.id, relMap, 0));

        // Assemble
        this.container.appendChild(div_header);
        this.container.appendChild(div_drillResults);
    }
};




AE_DrillPanel.prototype._buildDrill_recurse = function (entityId, anchorId, relMap, depth)
{
    // Self reference
    var that = this;

    // Grab list of relationships for given entity
    if (depth == 15)
        return DOMUtils.div("depth_limit","", "Entities below this depth not shown");

    else if (relMap.has(entityId))
    {
        var relationships = relMap.get(entityId);
        depth ++;

        // Remove relationships from map to prevent cycles
        relMap.remove(entityId);

        // Sort relationships
        relationships = _.sortBy(relationships, function (o) { return o.subject.name + o.object.name; });

        // Component - Relationship list
        var div = DOMUtils.div("relationships");
        for (var j = 0 ; j < relationships.length ; j++)
        {
            // Component - Subject entity
            var div_subject = DOMUtils.div("entity", "", relationships[j].subject.name);
            div_subject.entity = relationships[j].subject;
            if (this.scene.containsEntity(relationships[j].subject.id))
                $(div_subject).addClass("inScene");
            else if (this.config.isModelAvailable(relationships[j].subject.id))
                $(div_subject).addClass("outOfScene");
            else
                $(div_subject).addClass("noModel");
            if (anchorId == relationships[j].subject.id)
                $(div_subject).addClass("anchor");
            div_subject.onclick = function () {
                that.explorer.exploreEntity(this.entity.id);
            };

            // Component - Object entity
            var div_object = DOMUtils.div("entity", "", relationships[j].object.name);
            div_object.entity = relationships[j].object;
            if (this.scene.containsEntity(relationships[j].object.id))
                $(div_object).addClass("inScene");
            else if (this.config.isModelAvailable(relationships[j].object.id))
                $(div_object).addClass("outOfScene");
            else
                $(div_object).addClass("noModel");
            if (anchorId == relationships[j].object.id)
                $(div_object).addClass("anchor");
            div_object.onclick = function () {
                that.explorer.exploreEntity(this.entity.id);
            };

            // Component - Children
            var div_children = this._buildDrill_recurse(relationships[j].object.id, anchorId, relMap, depth);
            if (div_children)
                $(div_children).addClass("children");

            // Assemble
            var div_relationship = DOMUtils.div("relationship");
            div_relationship.appendChild(div_subject);
            div_relationship.appendChild(DOMUtils.div("arrow"));
            div_relationship.appendChild(div_object);
            if (div_children)
                div_relationship.appendChild(div_children);
            div.appendChild(div_relationship);
        }

        return div;
    }
    else
        return null;
};




AE_DrillPanel.prototype._evt_addEntitiesToScene = function (partials)
{
    // Self reference
    var that = this;

    // Use explorer to add entities to the scene
    this.scene.addEntitiesByPartial(partials, function ()
    {
        // Extract list of entitity IDs for repainting
        var entityIds = [];
        for (var i = 0 ; i < partials.length ; i ++)
            entityIds.push(partials[i].id);

        // Update lens
        that.lens.repaintEntities(partials);
    });
};




AE_DrillPanel.prototype._evt_addRelationshipsToScene = function (relationships)
{
    // Extract entity IDs from relationships
    var partials = [];
    for (var i = 0 ; i < relationships.length ; i ++)
    {
        partials.push({
            id: relationships[i].subject.id,
            name: relationships[i].subject.name,
        });
        partials.push({
            id: relationships[i].object.id,
            name: relationships[i].object.name,
        });
    }

    // Ensure no duplicates
    var partialsMap = {};
    for (var i = 0 ; i < partials.length ; i ++)
        partialsMap[partials[i].id] = partials[i];
    partials = [];
    for (var key in partialsMap)
        partials.push(partialsMap[key]);

    this._evt_addEntitiesToScene(partials);
};




AE_DrillPanel.prototype._evt_removeEntitiesFromSceneGivenRelationships = function (relationships)
{
    var entityIds = [];

    // Extract entity IDs from relationships
    for (var i = 0 ; i < relationships.length ; i ++)
    {
        entityIds.push(relationships[i].subject.id);
        entityIds.push(relationships[i].object.id);
    }

    // Ensure no duplicates
    entityIds = _.uniq(entityIds);

    // Ensure explored entity is not removed.
    var exploredEntityId = this.explorer.getExploredEntity().id;
    _.remove(entityIds, function (n) { return n == exploredEntityId; });

    // Remove entities from scene
    this.scene.removeEntities(entityIds);

    // Update scene
    this.buildUI();
};
