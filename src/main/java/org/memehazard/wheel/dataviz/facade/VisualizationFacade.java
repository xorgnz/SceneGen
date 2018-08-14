package org.memehazard.wheel.dataviz.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisualizationFacade
{
    @Autowired
    private QueryDAO            dao_query;
    @Autowired
    private QueryDispatchFacade facade_dispatch;
    private Logger              log = LoggerFactory.getLogger(getClass());


    public Collection<? extends Entity> retrieveDataFromQuery(int queryId, Map<String, String> parameters, Integer[] entityIds)
            throws ParserException, IOException
    {
        log.trace("Retrieving data from query " + queryId);
        log.trace("Entity IDs requested: " + StringUtils.join(entityIds, ", "));

        try
        {
            // Retrieve query
            Query q = dao_query.get(queryId);

            // Extract query parameter names
            QueryParameter param_entityId = null;
            QueryParameter param_entityIds = null;
            for (QueryParameter qp : q.getParameters())
                if (qp.isFlag_entityId())
                    param_entityId = qp;
                else if (qp.getTags().contains("entityIds"))
                    param_entityIds = qp;

            // SELECT - Method of execution
            // OPTION - Invoke query individually per entity ID
            if (param_entityId != null)
                return executeIndividually(q, entityIds, parameters, param_entityId);

            // OPTION - Invoke query jointly with all entity IDs
            else if (param_entityIds != null)
                return executeJointly(q, entityIds, parameters, param_entityIds);

            // OPTION - Invoke query irrespective of entity IDs
            else
                return executeWithoutParameters(q, parameters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<Entity>();
        }
    }


    private Collection<? extends Entity> executeJointly(Query q, Integer[] entityIds, Map<String, String> parameters, QueryParameter param)
            throws ParserException, IOException
    {
        // Compose parameter map
        parameters.put(param.getVariable(), StringUtils.join(entityIds, ","));

        // Execute query
        return facade_dispatch.retrieveEntitiesByQuery(q, parameters);
    }


    private Collection<? extends Entity> executeIndividually(Query q, Integer[] entityIds, Map<String, String> parameters, QueryParameter param)
            throws ParserException, IOException
    {
        List<Entity> entities = new ArrayList<Entity>();

        for (int id : entityIds)
        {
            parameters.put(param.getVariable(), "" + id);

            entities.addAll(facade_dispatch.retrieveEntitiesByQuery(q, parameters));
        }

        return entities;
    }


    private Collection<? extends Entity> executeWithoutParameters(Query q, Map<String, String> parameters)
            throws ParserException, IOException
    {
        return facade_dispatch.retrieveEntitiesByQuery(q, parameters);
    }
}
