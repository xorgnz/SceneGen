package org.memehazard.wheel.query.facade;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.query.facade.SpecialQueryRegistry.SpecialQuery;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;

public interface QueryDispatchFacade
{
    public List<Entity> retrieveAncestors(int entityId)
            throws ParserException, IOException;


    public List<Entity> retrieveEntitiesByQuery(Query q, Map<String, String> parameters)
            throws ParserException, IOException;


    public Entity retrieveEntity(int entityId)
            throws ParserException, IOException;


    public Entity retrieveEntityByName(String entityName)
            throws ParserException, IOException;


    public String retrieveQueryResponse(Query q, Map<String, String> parameters)
            throws IOException;


//    public List<Entity> retrieveRelatedEntities(int entityId, String relationshipType)
//            throws ParserException, IOException;


    public List<Relationship> retrieveRelationshipsByEntity(int entityId)
            throws ParserException, IOException;


    public List<Relationship> retrieveRelationshipsByEntityRelationCascade(int entityId, String relation)
            throws ParserException, IOException;


    public Map<String, Object> retrieveDataFromQuery(Query q, Map<String, String> parameters)
            throws ParserException, IOException;


    public String retrieveSpecialQueryResponse(SpecialQuery sq, Map<String, String> parameters)
            throws IOException;
}
