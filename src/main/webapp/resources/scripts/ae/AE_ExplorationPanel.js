/**************************************************************************
    Class - AE Relations Panel

    Shows list of relations and entities associated with a given entity

**************************************************************************/
var AE_ExplorationPanel = function (container, controller)
{
    this.container = container;
    this.explorer = controller.explorer;    // Exploration commands
    this.scene = controller.main.scene;     // Info on entities in scene
    this.config = controller.main.config;   // Info on model availability; Scene mod commands
    this.lens = controller.lens;            // Entity paint commands

    // HTML Element storage
    this.htmlElements = {};

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_ExplorationPanel");

    // Create initial UI
    this.buildUI([]);
    this._buildHeader(null);
};




// Create the UI for the relations panel
AE_ExplorationPanel.prototype.buildUI = function ()
{
    console.log("AE_ExplorationPanel.buildUI");

    this.clear();

    // Working notification
    this.htmlElements.div_working_notification = DOMUtils.div("workingNotification");
    this.container.appendChild(this.htmlElements.div_working_notification);

    // Panel - Header
    this.htmlElements.div_header = DOMUtils.div("header");
    this.container.appendChild(this.htmlElements.div_header);

    // Panel - Contents
    this.htmlElements.div_contents = DOMUtils.div("contents");
    this.container.appendChild(this.htmlElements.div_contents);

    // Storage - relation sub panels
    this.htmlElements.divs_relGroup = {};
};





AE_ExplorationPanel.prototype.consumeEvent = function (data, event)
{
    // OPTION - Work started
    if (event == AE_Explorer.EVENT_WORK_START)
    {
        this.htmlElements.div_working_notification.innerHTML = "Exploring..";
        $(this.htmlElements.div_working_notification).show();
    }

    // OPTION - Work ended
    else if (event == AE_Explorer.EVENT_WORK_END)
    {
        $(this.htmlElements.div_working_notification).hide();
    }

    // Work - Exploration changed
    else if (event == AE_Explorer.EVENT_EXP_CHANGED)
    {
        if (data.entity)
        {
            this._buildHeader(data.entity);
            this._buildContents(data.entity);
        }
        else
        {
            this._buildHeader(null);
            this._buildContents(null);
        }
    }

    // Work - Relation changed
    else if (event == AE_Explorer.EVENT_REL_CHANGED)
    {
        if (data.relName)
            this._buildHighlightedRelation(data.relName);
        else
            this._buildHighlightedRelation(null);
    }

    else if (event == AE_SceneManager.EVENT_UPDATED)
    {
        this.refreshUI();
    }
};




AE_ExplorationPanel.prototype.refreshUI = function ()
{
    var entity = this.explorer.getExploredEntity();
    var relation = this.explorer.getHighlightedRelation();

    this._buildHeader(entity);
    this._buildContents(entity);
    this._buildHighlightedRelation(relation ? relation.relation : null);
};




AE_ExplorationPanel.prototype._buildContents = function (entity)
{
    // Self reference
    var that = this;

    // Clear contents
    while (this.htmlElements.div_contents.firstChild)
        this.htmlElements.div_contents.removeChild(this.htmlElements.div_contents.firstChild);
    this.htmlElements.divs_relGroup = {};

    // If no relationships loaded, and exploration in progress
    if (entity && ! entity.relationships && this.explorer.isExploring())
        this.htmlElements.div_contents.appendChild(DOMUtils.div("", "", "Waiting for relationships to load..", "font-weight:bold; padding-left: 4px;"));

    // If no relationships loaded
    else if (entity && ! entity.relationships)
        this.htmlElements.div_contents.appendChild(DOMUtils.text("No relationships"));

    // Relationship list
    else if (entity)
    {
        // Create sorted list of unique relations
        var relations = _.pluck(entity.relationships, "relation");
        relations = _.uniq(relations, "relation");
        relations = _.sortBy(relations, "relation");

        for (var i = 0 ; i < relations.length ; i++)
        {
            // Extract sorted list of relationships
            var filteredRelationships = _.filter(entity.relationships, _.matchesProperty("relation", relations[i]));
            filteredRelationships = _.sortBy(filteredRelationships, function (o) { return o.subject.name + o.object.name; });

            // Component - Relation line
            var div_header = DOMUtils.div("block_header");
            div_header.relation = relations[i];

            // Component - Header
            var div_header_text = DOMUtils.div("text", "", relations[i].label ? relations[i].label : relations[i].relation);
            div_header_text.relName = relations[i].relation;
            div_header_text.onclick = function () {
                that._highlightRelation(entity.id, this.relName);
            };

            // Component - "Add" button
            var div_add_button = DOMUtils.div("button", null, "Add");
            div_add_button.relationships = filteredRelationships;
            div_add_button.onclick = function () {
                that._addRelationshipsToScene(this.relationships);
                that.lens.setSelectedEntity(entity.id);
            };

            // Component - "Remove" button
            var div_remove_button = DOMUtils.div("button", null, "Remove");
            div_remove_button.relationships = filteredRelationships;
            div_remove_button.onclick = function () {
                that._removeEntitiesFromSceneGivenRelationships(this.relationships);
            };

            // Component - "Drill" button
            var div_drill_button = DOMUtils.div("button", null, "Drill");
            div_drill_button.relName = relations[i].relation;
            div_drill_button.onclick = function () {
                that._drillRelation(entity.id, this.relName);
            };

            // Assemble - relation line
            div_header.appendChild(div_header_text);
            div_header.appendChild(div_drill_button);
            div_header.appendChild(div_remove_button);
            div_header.appendChild(div_add_button);

            // Component - Relationship list
            var div_relationships = DOMUtils.div("");
            for (var j = 0 ; j < filteredRelationships.length ; j++)
            {
                // Component - Subject entity
                var div_subject = DOMUtils.div("entity", "", filteredRelationships[j].subject.name);
                div_subject.entity = filteredRelationships[j].subject;
                if (this.scene.containsEntity(filteredRelationships[j].subject.id))
                    $(div_subject).addClass("inScene");
                else if (this.config.isModelAvailable(filteredRelationships[j].subject.id))
                    $(div_subject).addClass("outOfScene");
                else
                    $(div_subject).addClass("noModel");
                if (entity.id == filteredRelationships[j].subject.id)
                    $(div_subject).addClass("anchor");
                div_subject.onclick = function () {
                    that.explorer.exploreEntity(this.entity.id);
                };

                // Component - Object entity
                var div_object = DOMUtils.div("entity", "", filteredRelationships[j].object.name);
                div_object.entity = filteredRelationships[j].object;
                if (this.scene.containsEntity(filteredRelationships[j].object.id))
                    $(div_object).addClass("inScene");
                else if (this.config.isModelAvailable(filteredRelationships[j].object.id))
                    $(div_object).addClass("outOfScene");
                else
                    $(div_object).addClass("noModel");
                if (entity.id == filteredRelationships[j].object.id)
                    $(div_object).addClass("anchor");
                div_object.onclick = function () {
                    that.explorer.exploreEntity(this.entity.id);
                };

                // Assemble
                var div_relationship = DOMUtils.div("relationship");
                div_relationship.appendChild(div_subject);
                div_relationship.appendChild(DOMUtils.div("arrow"));
                div_relationship.appendChild(div_object);
                div_relationships.appendChild(div_relationship);
            }

            // Assemble - group
            var div_relGroup = DOMUtils.div("relationGroup");

            div_relGroup.appendChild(div_header);
            div_relGroup.appendChild(div_relationships);
            this.htmlElements.div_contents.appendChild(div_relGroup);

            // Store relGroup for access later
            this.htmlElements.divs_relGroup[relations[i].relation] = div_relGroup;
        }
    }
};




AE_ExplorationPanel.prototype._buildHeader = function (entity)
{
    // Self reference
    var that = this;

    // Erase previous header contents
    while (this.htmlElements.div_header.firstChild)
        this.htmlElements.div_header.removeChild(this.htmlElements.div_header.firstChild);

    if (entity)
    {
        // Component - Title
        this.htmlElements.div_header.appendChild(DOMUtils.header(1, entity.name + "(ID: " + entity.id + ")"));

        // Component - Scene Presence label and button
        var div_shown = DOMUtils.div("shown");;
        var div_shown_toggle = DOMUtils.div("button");
        if (this.scene.containsEntity(entity.id))
        {
            $(div_shown).addClass("inScene");
            div_shown.appendChild(DOMUtils.text("In scene"));

            div_shown_toggle.appendChild(DOMUtils.text("Remove from Scene"));
            div_shown_toggle.onclick = function () { that._removeEntityFromScene(entity.id); };
        }
        else if (this.config.isModelAvailable(entity.id))
        {
            $(div_shown).addClass("outOfScene");
            div_shown.appendChild(DOMUtils.text("Not in scene"));

            div_shown_toggle.appendChild(DOMUtils.text("Add to Scene"));
            div_shown_toggle.onclick = function () { that._evt_addEntitiesToScene([{
                id: entity.id,
                name: entity.name
            }]); };
        }
        else
        {
            $(div_shown).addClass("noModel");
            div_shown.appendChild(DOMUtils.text("Model not available"));

            div_shown_toggle = null;
        }

        // Assemble
        this.htmlElements.div_header.appendChild(div_shown);
        if (div_shown_toggle)
            this.htmlElements.div_header.appendChild(div_shown_toggle);
    }

    else
    {
        // Set blank header
        this.htmlElements.div_header.appendChild(DOMUtils.header(1, "No current exploration"));
    }
};




AE_ExplorationPanel.prototype._buildHighlightedRelation = function (relName)
{
    // Clear old highlight
    for (var key in this.htmlElements.divs_relGroup)
        $(this.htmlElements.divs_relGroup[key]).removeClass("highlight");

    // Highlight new relation sub panel
    if (relName)
    {
        var div_relGroup = this.htmlElements.divs_relGroup[relName];
        if (div_relGroup)
            $(div_relGroup).addClass("highlight");
        else
            throw "Unable to highlight relation " + relName + " - no sub panel exists";
    }
};




AE_ExplorationPanel.prototype._evt_addEntitiesToScene = function (partials)
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
        that.lens.repaintEntities(entityIds);
    });
};




AE_ExplorationPanel.prototype._addRelationshipsToScene = function (relationships)
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




AE_ExplorationPanel.prototype._drillRelation = function (entityId, relName)
{
    this.explorer.drillRelation(entityId, relName);
};




AE_ExplorationPanel.prototype._highlightRelation = function (entityId, relName)
{
    this.explorer.highlightEntityRelation(entityId, relName);
};




AE_ExplorationPanel.prototype._removeEntityFromScene = function (entityId)
{
    // Self reference
    this.scene.removeEntity(entityId);
};




AE_ExplorationPanel.prototype._removeEntitiesFromSceneGivenRelationships = function (relationships)
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
};

