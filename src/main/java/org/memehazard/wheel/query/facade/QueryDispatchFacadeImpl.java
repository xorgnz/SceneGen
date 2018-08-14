/**
 *
 */
package org.memehazard.wheel.query.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.memehazard.wheel.query.dao.QueryCacheLineDAO;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.SpecialQueryRegistry.SpecialQuery;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.query.parser.QueryIntegratorXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xorgnz
 * 
 */
@Component
public class QueryDispatchFacadeImpl implements QueryDispatchFacade
{
    private static final int     QCL_LIFESPAN = 604800;

    @Autowired
    private QueryCacheLineDAO    dao_qcl;
    @Autowired
    private QueryDAO             dao_query;

    private Logger               log          = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpecialQueryRegistry registry;


    public QueryDispatchFacadeImpl()
    {
    }


    @Override
    public List<Entity> retrieveAncestors(int entityId)
            throws ParserException, IOException
    {
        // Retrieve query
        Query q = registry.getSpecialQuery(SpecialQuery.GET_ANCESTORS);

        // Assemble parameters
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("args", "" + entityId);

        return parseEntitiesFromQueryResponse(executeQuery(q, parameters));
    }


    @Override
    public List<Entity> retrieveEntitiesByQuery(Query q, Map<String, String> parameters)
            throws ParserException, IOException
    {
        return parseEntitiesFromQueryResponse(executeQuery(q, parameters));
    }


    @Override
    public Entity retrieveEntity(int entityId)
            throws ParserException, IOException
    {
        Query q = registry.getSpecialQuery(SpecialQuery.GET_ENTITY_BY_FMAID);

        // Assemble parameters
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("args", "" + entityId);

        List<Entity> entities = parseEntitiesFromQueryResponse(executeQuery(q, parameters));

        if (entities.size() > 0)
            return entities.get(0);
        else
            return null;
    }


    @Override
    public Entity retrieveEntityByName(String entityName) throws ParserException, IOException
    {
        Query q = registry.getSpecialQuery(SpecialQuery.GET_ENTITY_BY_FMA_NAME);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("args", "" + entityName);

        List<Entity> entities = parseEntitiesFromQueryResponse(executeQuery(q, parameters));

        if (entities.size() > 0)
            return entities.get(0);
        else
            return null;
    }


    @Override
    public String retrieveQueryResponse(Query q, Map<String, String> parameters)
            throws IOException
    {
        return executeQuery(q, parameters);
    }


//    @Override
//    public List<Entity> retrieveRelatedEntities(int entityId, String relation)
//            throws ParserException, IOException
//    {
//        // Prepare query
//        Query q = registry.getSpecialQuery(SpecialQuery.GET_RELATED_ENTITIES);
//        Map<String, String> parameters = new HashMap<String, String>();
//        parameters.put("subject", "" + entityId);
//        parameters.put("relation", relation);
//
//        return parseEntitiesFromQueryResponse(executeQuery(q, parameters));
//    }


    @Override
    public List<Relationship> retrieveRelationshipsByEntity(int entityId)
            throws ParserException, IOException
    {
        // Compose parameter map
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("args", "" + entityId);

        // Execute query
        String response = retrieveSpecialQueryResponse(SpecialQuery.GET_RELATIONSHIPS, parameters);

        // Parse response
        return parseRelationshipsFromQueryResponse(response);
    }


    @Override
    public List<Relationship> retrieveRelationshipsByEntityRelationCascade(int entityId, String relation)
            throws ParserException, IOException
    {
        // Compose parameter map
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("target", "" + entityId);
        parameters.put("relation", relation);

        // Execute query
        String response = retrieveSpecialQueryResponse(SpecialQuery.GET_RELATIONSHIPS_BY_ER_CASCADE, parameters);

        // Parse response
        return parseRelationshipsFromQueryResponse(response);
    }


    @Override
    public String retrieveSpecialQueryResponse(SpecialQuery sq, Map<String, String> parameters) throws IOException
    {
        Query q = registry.getSpecialQuery(sq);

        return retrieveQueryResponse(q, parameters);
    }


    private String executeQuery(Query q, Map<String, String> parameters) throws IOException
    {
        log.trace("Executing query [" + q.getName() + "]" + " with param " + parameters);

        // Prep response variable
        String result = null;

        // Retrieve objects
        log.trace("Calling query " + q + " with params " + parameters);
        QueryCacheLine qcl = dao_qcl.getByQueryAndParam(q.getId(), QueryCacheLine.buildParameterValueString(parameters));
        log.trace(qcl != null ? "Found matching QCL: " + qcl : "No matching QCL");

        // Test cache recency
        if (qcl != null)
        {
            Calendar cal_threshold = Calendar.getInstance();
            cal_threshold.add(Calendar.SECOND, -QCL_LIFESPAN);

            Calendar cal_retrieved = Calendar.getInstance();
            cal_retrieved.setTimeInMillis(qcl.getRetrieved().getTime());
            if (cal_threshold.before(cal_retrieved))
            {
                log.trace("Cache hit! Skipping Query Integrator request");
                result = qcl.getResult();
            }
            else
                log.trace("Cache miss!. Issuing Query Integrator request");
        }

        // If no acceptable result from cache found
        if (result == null)
        {
            // Issue query
            result = issueQuery(q, parameters);

            // If no QCL exists, create
            if (qcl == null)
            {
                qcl = new QueryCacheLine();
                qcl.setQuery(q);
                qcl.setResult(result);
                qcl.setRetrieved(new Date());
                qcl.setParamValueMap(parameters);
                dao_qcl.add(qcl);
            }
            // Otherwise, update QCL
            else
            {
                qcl.setResult(result);
                qcl.setRetrieved(new Date());
                qcl.setParamValueMap(parameters);
                dao_qcl.update(qcl);
            }
        }

        return result;
    }


    private String issueQuery(Query q, Map<String, String> parameters)
            throws IOException
    {
        // Build Query Integrator URL
        URIBuilder ub_query = new URIBuilder();
        ub_query.setHost("xiphoid.biostr.washington.edu");
        ub_query.setPort(8080);
        ub_query.setScheme("http");
        ub_query.setParameter("qid", "" + q.getQueryId());
        for (String s : parameters.keySet())
            ub_query.setParameter(s, "" + parameters.get(s));
        if (parameters.size() == 1 && parameters.containsKey("args"))
            ub_query.setPath("/QueryManager/TemplateQueryResults");
        else
            ub_query.setPath("/QueryManager/ParamQueryResults");

        // Build HTTP request
        try
        {
            URI uri = ub_query.build();
            HttpGet request = new HttpGet(uri);

            // Issue query request
            log.trace("Issuing query request to QI service: " + request.getURI().toString());
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringWriter out = new StringWriter();
            IOUtils.copy(in, out);
            return out.getBuffer().toString();
        }
        catch (URISyntaxException e)
        {
            throw new IOException("Cannot form query UIR", e);
        }
    }


    private List<Entity> parseEntitiesFromQueryResponse(String response)
            throws ParserException
    {
        QueryIntegratorXmlParser parser = new QueryIntegratorXmlParser();
        return parser.parseEntities(response);
    }


    private List<Relationship> parseRelationshipsFromQueryResponse(String response)
            throws ParserException
    {
        QueryIntegratorXmlParser parser = new QueryIntegratorXmlParser();
        return parser.parseRelationships(response);
    }


    @Override
    public Map<String, Object> retrieveDataFromQuery(Query q, Map<String, String> parameters)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
