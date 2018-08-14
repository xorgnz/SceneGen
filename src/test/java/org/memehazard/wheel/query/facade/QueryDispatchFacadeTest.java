package org.memehazard.wheel.query.facade;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.dao.QueryCacheLineDAO;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacadeImpl;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.memehazard.wheel.query.model.QueryParameter;
import org.memehazard.wheel.query.test.TestDAO_Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class QueryDispatchFacadeTest
{
    private static final String    QCL_PARAM_VARIABLE = "args";
    private static final String    QCL_PARAM_VALUE    = "Right_temporal_bone";
    private static final String    QCL_PARAM_STRING   = "args=Right_temporal_bone";

    @Autowired
    public QueryCacheLineDAO       dao_qcl;
    @Autowired
    public QueryDAO                dao_query;
    @Autowired
    public QueryDispatchFacadeImpl service;
    @Autowired
    private TestDAO_Query          dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    /**
     * This test will fail if the query integrator is inaccessible or query 293 is broken
     */
    @Test
    public void test_executeQuery() throws IOException
    {
        // Act - New request
        Query q = new Query("Articulation (FMAID)", "", 297, ""); 
        q.addParameter(new QueryParameter("args", "FMAID", new HashSet<String>())); 
        dao_query.add(q);
                
        Map<String, String> paramValueMap = new TreeMap<String, String>();
        paramValueMap.put(QCL_PARAM_VARIABLE, QCL_PARAM_VALUE);
        service.retrieveQueryResponse(q, paramValueMap);

        // Test - Is a cache line created?
        Assert.assertEquals(1, dao_qcl.listAll().size());
        QueryCacheLine qcl = dao_qcl.getByQueryAndParam(q.getId(), QCL_PARAM_STRING);

        // Test - are additional cache lines created?
        service.retrieveQueryResponse(q, paramValueMap);
        service.retrieveQueryResponse(q, paramValueMap);
        Assert.assertEquals(1, dao_qcl.listAll().size());

        // Is the original cache line still in use?
        QueryCacheLine qcl_copy = dao_qcl.getByQueryAndParam(q.getId(), QCL_PARAM_STRING);
        Assert.assertEquals(qcl.getId(), qcl_copy.getId());
    }
}