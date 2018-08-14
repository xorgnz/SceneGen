package org.memehazard.wheel.query.dao;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.memehazard.wheel.query.test.TestDAO_Query;
import org.memehazard.wheel.query.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class QueryCacheLineDAOTest_getByQueryAndParam
{
    @Autowired
    private QueryCacheLineDAO dao;

    @Autowired
    private QueryDAO          dao_query;

    @Autowired
    private TestDAO_Query     dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    public void test_getByQueryAndParam()
    {
        // PREP - Create objects
        Query q0 = new Query(TestData.Q_NAMES[0], TestData.Q_DESCRIPTIONS[0], TestData.Q_QIDS[0], TestData.Q_TAGS[0]);
        Query q1 = new Query(TestData.Q_NAMES[1], TestData.Q_DESCRIPTIONS[1], TestData.Q_QIDS[1], TestData.Q_TAGS[1]);
        dao_query.add(q0);
        dao_query.add(q1);
        QueryCacheLine qcl0 = new QueryCacheLine(q0, TestData.QCL_RESULTS[0], TestData.QCL_RETRIEVED(0), TestData.QCL_PARAM_VALS[0]);
        QueryCacheLine qcl1 = new QueryCacheLine(q0, TestData.QCL_RESULTS[1], TestData.QCL_RETRIEVED(1), TestData.QCL_PARAM_VALS[1]);
        QueryCacheLine qcl2 = new QueryCacheLine(q1, TestData.QCL_RESULTS[2], TestData.QCL_RETRIEVED(2), TestData.QCL_PARAM_VALS[2]);
        dao.add(qcl0);
        dao.add(qcl1);
        dao.add(qcl2);

        // ACT
        QueryCacheLine qcl2_copy = dao.getByQueryAndParam(q1.getId(), TestData.QCL_PARAM_VALS[2]);

        // Test - Correct QCL retrieved
        Assert.assertEquals(qcl2.getId(), qcl2_copy.getId());

        // ACT
        QueryCacheLine qcl_null = dao.getByQueryAndParam(q0.getId(), TestData.QCL_PARAM_VALS[4]);

        // Test - Do we get null if we search for a QCL that doesn't exist?
        Assert.assertNull(qcl_null);
    }
}