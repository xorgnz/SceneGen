package org.memehazard.wheel.query.facade;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.query.dao.QueryCacheLineDAO;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class QueryFacade
{
    @Autowired
    private QueryCacheLineDAO       dao_qcl;
    @Autowired
    private QueryDAO                dao_query;
    // private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QueryDispatchFacadeImpl svc_query;


    public void addQuery(Query q)
    {
        dao_query.add(q);

        if (q.getParameters().size() > 0)
            dao_query.addParameters(q.getId(), q.getParameters());
    }


    public void clearCache()
    {
        dao_qcl.deleteAll();
    }


    public void clearCacheByQuery(int qid)
    {
        dao_qcl.deleteByQuery(qid);
    }


    public void deleteQuery(int id)
    {
        dao_query.delete(id);
    }


    public void deleteQueryCacheLine(int id)
    {
        dao_qcl.delete(id);
    }


    public Query getQuery(int id)
    {
        return dao_query.get(id);
    }


    public QueryCacheLine getQueryCacheLine(int id)
    {
        return dao_qcl.get(id);
    }


    public List<Query> listQueries()
    {
        return dao_query.listAll();
    }


    public String runQuery(Query q, Map<String, String> paramMap)
            throws IOException
    {
        return svc_query.retrieveQueryResponse(q, paramMap);
    }


    public void updateQuery(Query q)
    {
        dao_query.update(q);

        if (q.getParameters().size() > 0)
            dao_query.updateParameters(q.getId(), q.getParameters());
        else
            dao_query.deleteParametersByQuery(q.getId());

    }


    public Query getQueryWithCacheLines(int queryId)
    {
        Query q = dao_query.get(queryId);
        q.setCacheLines(dao_qcl.listByQuery(queryId));

        return q;
    }

}
