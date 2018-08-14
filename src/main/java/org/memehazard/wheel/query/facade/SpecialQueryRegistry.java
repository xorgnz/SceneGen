package org.memehazard.wheel.query.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.memehazard.Helper;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecialQueryRegistry
{
    public static enum SpecialQuery
    {
        GET_ALL_ENTITY_NAMES,
        GET_ANCESTORS,
        GET_ENTITY_BY_FMAID,
        GET_ENTITY_BY_FMA_NAME,
        GET_RELATIONSHIPS,
        GET_RELATIONSHIPS_BY_ER_CASCADE,
        DATA_QUERY_137,
        DATA_QUERY_188
    }

    private static final Map<SpecialQuery, Integer>              QUERY_IDS          = new HashMap<SpecialQuery, Integer>();
    private static final Map<SpecialQuery, String>              QUERY_DESCRIPTIONS = new HashMap<SpecialQuery, String>();
    private static final Map<SpecialQuery, String>               QUERY_NAMES        = new HashMap<SpecialQuery, String>();
    private static final Map<SpecialQuery, String>               QUERY_TAGS         = new HashMap<SpecialQuery, String>();
    private static final Map<SpecialQuery, List<QueryParameter>> QUERY_PARAMS       = new HashMap<SpecialQuery, List<QueryParameter>>();

    @Autowired
    private QueryDAO                                             dao_query;


    public SpecialQueryRegistry()
    {
        HashSet<String> tags_entityId = new HashSet<String>();
        tags_entityId.add("entityId");
        HashSet<String> tags_entityName = new HashSet<String>();
        tags_entityName.add("entityName");


        // Get Ancestors - retrieves all ancestor entities of a given entity
        QUERY_IDS.put(SpecialQuery.GET_ANCESTORS, 324);
        QUERY_NAMES.put(SpecialQuery.GET_ANCESTORS, "SYS - Get Ancestors");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_ANCESTORS, "System query");        
        QUERY_TAGS.put(SpecialQuery.GET_ANCESTORS, "entities");
        QUERY_PARAMS.put(SpecialQuery.GET_ANCESTORS, Helper.asList(
                new QueryParameter("args", "FMAID", tags_entityId))); // g


        // Get Entity by FMAID
        QUERY_IDS.put(SpecialQuery.GET_ENTITY_BY_FMAID, 384);
        QUERY_NAMES.put(SpecialQuery.GET_ENTITY_BY_FMAID, "SYS - Get Entity by FMAID");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_ENTITY_BY_FMAID, "System query");        
        QUERY_TAGS.put(SpecialQuery.GET_ENTITY_BY_FMAID, "entities");
        QUERY_PARAMS.put(SpecialQuery.GET_ENTITY_BY_FMAID, Helper.asList(
                new QueryParameter("args", "FMAID", tags_entityId))); // g


        // Get Entity by Name
        QUERY_IDS.put(SpecialQuery.GET_ENTITY_BY_FMA_NAME, 402);
        QUERY_NAMES.put(SpecialQuery.GET_ENTITY_BY_FMA_NAME, "SYS - Get Entity by Label");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_ENTITY_BY_FMA_NAME, "System query");
        QUERY_TAGS.put(SpecialQuery.GET_ENTITY_BY_FMA_NAME, "entities");
        QUERY_PARAMS.put(SpecialQuery.GET_ENTITY_BY_FMA_NAME, Helper.asList(
                new QueryParameter("args", "FMA Entity Label", tags_entityName))); // g

        //
        // This query is no longer used.
        //
        // // Get all relationships from a given origin entity with a given relation type
        // QUERY_IDS.put(SpecialQuery.GET_RELATED_ENTITIES, 357);
        // QUERY_NAMES.put(SpecialQuery.GET_RELATED_ENTITIES, "SYS - Get Entities by Entity and Relation");
        // QUERY_PARAMS.put(SpecialQuery.GET_RELATED_ENTITIES, Helper.asList(
        // new QueryParameter("subject", "FMAID", ""),
        // new QueryParameter("relation", "Relationship", "")));


        // Get all relationships from a given origin entity with a given relation type, cascading downwards
        QUERY_IDS.put(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, 408);
        QUERY_NAMES.put(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, "SYS - Get Relationships by Entity and Relation, with cascade");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, "System query");
        QUERY_TAGS.put(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, "relationships");
        QUERY_PARAMS.put(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, Helper.asList(
                new QueryParameter("target", "FMAID", tags_entityId),
                new QueryParameter("relation", "Relationship", new HashSet<String>()))); // g

        // Get all relationships for which a given entity is subject or object
        QUERY_IDS.put(SpecialQuery.GET_RELATIONSHIPS, 383);
        QUERY_NAMES.put(SpecialQuery.GET_RELATIONSHIPS, "SYS - Get Relationships");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_RELATIONSHIPS, "System query");
        QUERY_TAGS.put(SpecialQuery.GET_RELATIONSHIPS, "relationships");
        QUERY_PARAMS.put(SpecialQuery.GET_RELATIONSHIPS, Helper.asList(
                new QueryParameter("args", "Target Object (FMAID)", tags_entityId))); // g

        // SYS - Get all rdfs:label values for use in text completion
        QUERY_IDS.put(SpecialQuery.GET_ALL_ENTITY_NAMES, 418);
        QUERY_NAMES.put(SpecialQuery.GET_ALL_ENTITY_NAMES, "SYS - Extract names for text completion");
        QUERY_DESCRIPTIONS.put(SpecialQuery.GET_ALL_ENTITY_NAMES, "System query");
        QUERY_TAGS.put(SpecialQuery.GET_ALL_ENTITY_NAMES, "special");
        QUERY_PARAMS.put(SpecialQuery.GET_ALL_ENTITY_NAMES, new ArrayList<QueryParameter>());
    }


    public Query getSpecialQuery(SpecialQuery sq)
    {
        Query q = dao_query.findByQueryId(QUERY_IDS.get(sq));

        // Create query if missing
        if (q == null)
        {
            q = new Query();
            q.setName(QUERY_NAMES.get(sq));
            q.setQueryId(QUERY_IDS.get(sq));
            q.setTags(QUERY_TAGS.get(sq));
            q.setDescription(QUERY_DESCRIPTIONS.get(sq));
            for (QueryParameter qp : QUERY_PARAMS.get(sq))
                q.addParameter(qp);

            dao_query.add(q);
        }

        return q;
    }
}
