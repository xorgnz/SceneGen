/**************************************************************************
    Class - AE Explorer

    Keeps track of entity exploration states . That is, selections, hovering, and focus
    Triggers changes to relations panel

************************************************************************************/
var AE_Explorer = function(main, historian, lens, entityRegistry)
{
    // Associations
    this.communicator = main.communicator;      // Issue server requests
    this.config = main.config;                  // Lookup relations
    this.historian = historian;                 // Track history
    this.lens = lens;                           // Manage entity state display
    this.entityRegistry = entityRegistry;       // Manage entities once loaded

    // Configuration
    this.assetSetId = main.config.assetSetId;
    this.stylesheetId = main.config.stylesheetId;

    // State variables
    this._clearExploration();

    // Flags
    this.flag_working = false;

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "AE_Explorer");
};
// Constants - Explorer modes
AE_Explorer.EXP_NONE = 0;
AE_Explorer.EXP_ENTITY = 1;




// Event types
AE_Explorer.EVENT_ERROR         = "ERROR";
AE_Explorer.EVENT_WORK_START    = "WORK_START";
AE_Explorer.EVENT_WORK_END      = "WORK_END";
AE_Explorer.EVENT_EXP_CHANGED   = "EXP_CHANGED";
AE_Explorer.EVENT_REL_CHANGED   = "REL_CHANGED";
AE_Explorer.EVENT_DRILL_CHANGED   = "DRILL_CHANGED";




// Accessors
AE_Explorer.prototype.getDrilledRelation        = function () { return this.drilledRelation; };
AE_Explorer.prototype.getExploredEntity         = function () { return this.entityRegistry.getEntityById(this.exploredEntityId); };
AE_Explorer.prototype.getHighlightedRelation    = function () { return this.highlightedRelation; };
AE_Explorer.prototype.isEntityExplored          = function () { return this.exploredEntityId != null; };
AE_Explorer.prototype.isExploring               = function () { return this.flag_working; };
AE_Explorer.prototype.isRelationHighlighted     = function () { return this.highlightedRelation != null; };
AE_Explorer.prototype.isRelationDrilled         = function () { return this.drilledRelation.relation != null; };




AE_Explorer.prototype.drillRelation = function (entityId, relName)
{
    console.log("AE_Explorer.drillRelation - entityID: " + entityId + ", relName: " + relName);

    // Self reference
    var that = this;

    // If no relation name is passed, revert to exploring entity
    if (! relName || ! entityId)
    {
        console.log("AE_Explorer.drillRelation - Revert - relName or entityId null");
        this.exploreEntity(entityId);
    }

    // Entity explored - do nothing
    else if (this.exploredEntity == entityId && this.drilledRelation.relation == relName)
    {
        console.log("AE_Explorer.drillRelation - Do nothing - relation already highlighted");
    }

    // Proceed
    else
    {
        // Set exploration flag
        this.flag_working = true;

        // EVENTs
        that.fireEvent(null, AE_Explorer.EVENT_WORK_START);

        // Clear exploration
        this._clearExploration();
        this.exploredEntityId = entityId;
        that.drilledRelation = {
            relation: relName,
            relationships: null,
        };

        this.communicator
            .invoke_relationship__loadByEntityRelationCascade(entityId, relName)
            .done(function (data)
            {
                console.log("AE_Communicator.invoke_relationship__loadByEntityRelationCascade - response:");
                console.log(data);

                if (data.success)
                {
                    console.log("AE_Explorer.drillRelation - Success!");

                    // Store highlight
                    that.exploredEntityId = entityId;
                    that.drilledRelation = {
                        relation: relName,
                        relationships: data.relationships,
                    };

                    // Clear exploration flag
                    that.flag_working = false;

                    // Issue update event
                    that.fireEvent({
                            entity: that.entityRegistry.getEntityById(entityId),
                            relName: relName,
                            relationships: data.relationships
                        }, AE_Explorer.EVENT_DRILL_CHANGED);
                    that.fireEvent(null, AE_Explorer.EVENT_WORK_END);
                }
                else
                {
                    that.fireEvent({message:"Drill failed. Server error. See server log for details"}, AE_Explorer.EVENT_ERROR);
                }
            });
    }
};




// Explore entity with given ID
//
// Return true if exploration performed, false otherwise
AE_Explorer.prototype.exploreEntity = function (entityId, callback) // events
{
    console.log("AE_Explorer.exploreEntity - entityID: " + entityId);

    // Self reference
    var that = this;

    // Retrieve entity
    var entity = this.entityRegistry.getEntityById(entityId);

    // OPTION - already exploring - wait and try again later
    if (this.flag_working)
    {
        console.log("AE_Explorer.exploreEntity - Delay - already exploring");

        window.setTimeout(function () { that.exploreEntity(entityId, callback); }, 500);
    }

    // OPTION - null entity ID - clear exploration
    else if (entityId == null)
    {
        console.log("AE_Explorer.exploreEntity - Clear - null entity passed");

        // Save blank exploration
        this._clearExploration();

        // Clear lens
        this.lens.setFocusedEntities(null, []);

        // Notify listeners
        this.fireEvent(null, AE_Explorer.EVENT_EXP_CHANGED);

        if (callback)
            callback();
    }

    // OPTION - entity already explored - do nothing
    else if (this.exploredEntityId == entityId && ! this.isRelationDrilled())
    {
        console.log("AE_Explorer.exploreEntity - Do nothing - entity already explored");

        if (callback)
            callback();
    }

    // OPTION - entity not in registry - Load entity and re-trigger exploration
    else if (! entity)
        this.loadEntity(entityId, function () { that.exploreEntity(entityId, callback); });

    // OPTION - Explore
    else
    {
        console.log("AE_Explorer.exploreEntity - starting");

        // Set exploration flag
        this.flag_working = true;

        // Clear previous exploration
        this._clearExploration();
        this.exploredEntityId = entityId;

        // EVENTs
        this.fireEvent(null, AE_Explorer.EVENT_WORK_START);
        this.fireEvent({entity: entity}, AE_Explorer.EVENT_EXP_CHANGED);

        // Add exploration event to history
        this.historian.addExploreEvent(entity.id, entity.name);

        // Load relationships for entity
        that._loadRelationships(entity, function ()
        {
            console.log("AE_Explorer.exploreEntity - Success: " + entityId);

            // Store exploration
            that.exploredEntityId = entity.id;

            // Update lens
            that.lens.setFocusedEntities(null, []);
            that.lens.setSelectedEntity(entity.id);

            // Clear exploration flag
            that.flag_working = false;

            // EVENTs
            that.fireEvent(null, AE_Explorer.EVENT_WORK_END);
            that.fireEvent({entity: entity}, AE_Explorer.EVENT_EXP_CHANGED);

            if (callback)
                callback();
        });
    }
};




AE_Explorer.prototype.exploreEntityByName = function (entityName) // events
{
    // Self Reference
    var that = this;

    // Clear exploration
    that._clearExploration();
    this.fireEvent(null, AE_Explorer.EVENT_EXP_CHANGED);

    // Lookup entity with given name, then explore this entity
    this.loadEntityByName(entityName, function (entity) {
        if (entity)
            that.exploreEntity(entity.id);
        else
        {
            that.fireEvent({message: "No entity found with name '" + entityName + "'"}, AE_Explorer.EVENT_ERROR);
        }
    });
};




AE_Explorer.prototype.highlightEntityRelation = function (entityId, relName, callback)
{
    console.log("AE_Explorer.highlightEntityRelation - entityID: " + entityId + ", relName: " + relName);

    // Self reference
    var that = this;

    // Either entity or relation is null. Clear exploration
    if (! entityId || ! relName)
    {
        console.log("AE_Explorer.highlightEntityRelation - Clear - null entity or relation passed");

        // Save blank exploration
        this._clearExploration();

        // Clear lens
        this.lens.setFocusedEntities(null, []);

        // Notify listeners
        this.fireEvent(null, AE_Explorer.EVENT_EXP_CHANGED);
        that.fireEvent(null, AE_Explorer.EVENT_REL_CHANGED);

        if (callback)
            callback();
    }


    if (! relName && ! entityId)
    {
        console.log("AE_Explorer.highlightEntityRelation - Revert - relName or entityId null");
        this.exploreEntity(entityId);
    }

    // Entity explored - do nothing
    else if (this.exploredEntity == entityId && this.highlightedRelation == relName)
    {
        console.log("AE_Explorer.highlightEntityRelation - Do nothing - relation already highlighted");

        if (callback)
            callback();
    }

    // Proceed
    else
    {
        // Retrieve entity.
        var entity = this.entityRegistry.getEntityById(entityId);
        var relation = this.config.findRelation(relName);

        this.exploreEntity(entityId, function ()
        {
            console.log("AE_Explorer.highlightEntityRelation - Success");

            // Store highlight
            that.highlightedRelation = relation;

            // Extract list of entities to highlight
            var fRels = _.filter(entity.relationships, function (o) { return o.relation.relation == relName; });
            var relatedEntityIds = [];
            for (var i = 0 ; i < fRels.length ; i++)
                relatedEntityIds.push(fRels[i].subject.id == entityId ? fRels[i].object.id : fRels[i].subject.id);

            // Highlight objects in scene
            that.lens.setFocusedEntities(entityId, relatedEntityIds);

            // Issue update event
            that.fireEvent({relName: relName}, AE_Explorer.EVENT_REL_CHANGED);
        });
    }
};




AE_Explorer.prototype.highlightEntityRelationWithEntityName = function (entityName, relName)
{
    // Self Reference
    var that = this;

    // Lookup entity with given name, then explore this entity
    this.loadEntityByName(entityName, function (entity) {
        that.highlightEntityRelation(entity.id, relName);
    });
};




AE_Explorer.prototype.loadEntity = function (entityId, callback) // events
{
    console.log("AE_Explorer.loadEntity - entityID: " + entityId);

    // Self reference
    var that = this;

    // Raise flag
    this.flag_working = true;

    // EVENT - Work start
    this.fireEvent(null, AE_Explorer.EVENT_WORK_START);

    this.communicator
        .invoke_entity__load(entityId, this.assetSetId, this.stylesheetId)
        .done(function (data)
        {
            console.log("AE_Communicator.invoke_entity__load - response:");
            console.log(data);

            if (data.success)
            {
                entity = new AE_Entity(
                    data.entity.id,
                    data.entity.name,
                    data.entity.filename,
                    "",
                    data.entity.style);

                that.entityRegistry.registerEntities([entity]);

                // Lower flag
                that.flag_working = false;

                // EVENT - Work end
                that.fireEvent(null, AE_Explorer.EVENT_WORK_END);

                if (callback)
                    callback(entity);
            }
            else
                throw "Unable to load entity, ID: " + entityId +". See console for details";
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_entity__load - request failed: " + textStatus);
            console.log(errorThrown);

            throw "Unable to load entity, ID: " + entityId +". See console for details";
        });
};




AE_Explorer.prototype.loadEntityByName = function (name, callback) // events
{
    console.log("AE_Explorer.loadEntityByName - name: " + name);

    // Self reference
    var that = this;

    // Raise flag
    this.flag_working = true;

    // EVENT - Work start
    this.fireEvent(null, AE_Explorer.EVENT_WORK_START);

    this.communicator
        .invoke_entity__loadByName(name, this.assetSetId, this.stylesheetId)
        .done(function (data)
        {
            console.log("AE_Communicator.invoke_entity__loadByName - response:");
            console.log(data);

            if (data.success)
            {
                // Create entity from response
                var entity = null;
                if (data.entity)
                {
                    // Save entity
                    entity = new AE_Entity(
                        data.entity.id,
                        data.entity.name,
                        data.entity.filename,
                        "",
                        data.entity.style);
                    that.entityRegistry.registerEntities([entity]);
                }

                // Lower flag
                that.flag_working = false;

                // EVENT - Work end
                that.fireEvent(null, AE_Explorer.EVENT_WORK_END);

                if (callback)
                    callback(entity);
            }
            else
                throw "Unable to load entity, Name: " + name +". See console for details";
        })
        .error(function (jqXHR, textStatus, errorThrown)
        {
            console.log("AE_Communicator.invoke_entity__loadByName - request failed: " + textStatus);
            console.log(errorThrown);

            throw "Unable to load entity, Name: " + name +". See console for details";
        });
};




AE_Explorer.prototype.loadEntitiesFromId = function (entityIds, callback)
{
    console.log("AE_Explorer.loadEntitiesFromId");
    console.log(entityIds);

    // Self reference
    var that = this;

    // Create list of entities to load afresh
    var entityIds_toLoad = [];
    for (var i = 0 ; i < entityIds.length ; i ++)
    {
        var e = this.entityRegistry.getEntityById(entityIds[i]);

        if (! e)
            entityIds_toLoad.push(entityIds[i]);
    }

    // OPTION - No load required
    if (entityIds_toLoad.length == 0)
    {
        if (callback)
            callback();
    }

    // OPTION - Load entities
    else
    {
        console.log("AE_Explorer.loadEntitiesFromId - loading entity descriptors from server");
        console.log(entityIds_toLoad);

        // Raise flag
        this.flag_working = true;

        // EVENT - Work start
        this.fireEvent(null, AE_Explorer.EVENT_WORK_START);

        // Load entity descriptors from server
        this.communicator
            .invoke_entity__loadSeveral(this.stylesheetId, this.assetSetId, entityIds_toLoad)
            .done( function (data)
            {
                console.log("AE_Communicator.invoke_entity__loadSeveral - response:");
                console.log(data);

                // Create AE_Entity objects from newly loaded entity data
                var entities_newlyLoaded = [];
                for (var i = 0 ; i < data.entities.length ; i++)
                {
                    var obj = data.entities[i];
                    entities_newlyLoaded.push(new AE_Entity(
                        obj.id,
                        obj.name,
                        obj.filename,
                        "",
                        obj.style));
                }

                // Register newly created entities with registry
                that.entityRegistry.registerEntities(entities_newlyLoaded);

                // Lower flag
                that.flag_working = false;

                // EVENT - Work end
                that.fireEvent(null, AE_Explorer.EVENT_WORK_END);

                if (callback)
                    callback();
            });
    }
};




AE_Explorer.prototype.loadEntitiesFromPartial = function (partials, callback)
{
    console.log("AE_Explorer.loadEntitiesFromPartial");
    console.log(partials);

    // Self reference
    var that = this;

    // Create list of entities to load afresh
    var partials_toLoad = [];
    for (var i = 0 ; i < partials.length ; i ++)
    {
        var e = this.entityRegistry.getEntityById(partials[i].id);

        if (! e)
            partials_toLoad.push(partials[i]);
    }

    // Complete trivially if no entities are passed
    if (partials_toLoad.length == 0)
    {
        if (callback)
            callback();
    }
    else
    {
        console.log("AE_Explorer.loadEntitiesFromPartial - loading entity descriptors from server");
        console.log(partials_toLoad);

        // Raise flag
        this.flag_working = true;

        // EVENT - Work start
        this.fireEvent(null, AE_Explorer.EVENT_WORK_START);

        // Load entity descriptors from server
        this.communicator
            .invoke_entity__fromPartials(this.stylesheetId, this.assetSetId, partials_toLoad)
            .done( function (data)
            {
                console.log("AE_Communicator.invoke_entity__fromPartials - response:");
                console.log(data);

                // Create AE_Entity objects from newly loaded entity data
                var entities_newlyLoaded = [];
                for (var i = 0 ; i < data.entities.length ; i++)
                {
                    var obj = data.entities[i];
                    entities_newlyLoaded.push(new AE_Entity(
                        obj.id,
                        obj.name,
                        obj.filename,
                        "",
                        obj.style));
                }

                // Register newly created entities with registry
                that.entityRegistry.registerEntities(entities_newlyLoaded);

                // Lower flag
                that.flag_working = false;

                // EVENT - Work end
                that.fireEvent(null, AE_Explorer.EVENT_WORK_END);

                if (callback)
                    callback();
            });
    }
};




AE_Explorer.prototype._clearExploration = function ()
{
    this.exploredEntityId = null;
    this.highlightedRelation = null;
    this.drilledRelation = { relation: null, relationships: [] };
};




AE_Explorer.prototype._loadRelationships = function (entity, callback)
{
    // Self reference
    var that = this;

    // Relationship load needed, perform
    if (! entity.relationships)
    {
        // Raise flag
        this.flag_working = true;

        // Load relationships from server
        this.communicator
            .invoke_relationship__loadByEntity(entity.id)
            .done(function (data)
            {
                console.log("AE_Communicator.invoke_relationship__loadByEntity - response:");
                console.log(data);

                if (data.success)
                {
                    // Save relationships
                    entity.relationships = data.relationships;

                    // Lower flag
                    that.flag_working = false;

                    // Trigger callback
                    if (callback)
                        callback();
                }
                else
                    throw ("Exploration failure. See console for details");
            })
            .error(function (jqXHR, textStatus, errorThrown)
            {
                console.log("AE_Communicator.invoke_relationship__loadByEntity - request failed: " + textStatus);
                console.log(errorThrown);

                throw ("Exploration failure. See console for details");
            });
    }

    // No load needed - trigger callback
    else if (callback)
    {
        callback();
    }
};


