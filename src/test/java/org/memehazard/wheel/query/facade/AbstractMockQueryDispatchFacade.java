package org.memehazard.wheel.query.facade;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.query.facade.SpecialQueryRegistry.SpecialQuery;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;

/**
 * Abstract implementation of <code>QueryDispatchService</code>. All methods throw exceptions if not overriden.
 * 
 * This class is used by mock <code>QueryDispatchService</code> inner classes to obviate the need for stub methods.
 * 
 * @author xorgnz
 * 
 */
public abstract class AbstractMockQueryDispatchFacade implements QueryDispatchFacade
{
    @Override
    public List<Entity> retrieveAncestors(int fmaid)
            throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public List<Entity> retrieveEntitiesByQuery(Query q, Map<String, String> parameters)
            throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public Entity retrieveEntity(int entityId)
            throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public Entity retrieveEntityByName(String name)
            throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public String retrieveQueryResponse(Query q, Map<String, String> parameters)
            throws IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


//    @Override
//    public List<Entity> retrieveRelatedEntities(int entityId, String relationshipType)
//            throws ParserException, IOException
//    {
//        throw new RuntimeException("Mock object - method not implemented");
//    }


    @Override
    public List<Relationship> retrieveRelationshipsByEntity(int entityId) throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public List<Relationship> retrieveRelationshipsByEntityRelationCascade(int entityId, String relation) throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    public Map<String, Object> retrieveDataFromQuery(Query q, Map<String, String> parameters) throws ParserException, IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }


    @Override
    public String retrieveSpecialQueryResponse(SpecialQuery sq, Map<String, String> parameters)
            throws IOException
    {
        throw new RuntimeException("Mock object - method not implemented");
    }
}
