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
public class QueryCacheLineDAOTest_crud
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
    public void test_crud()
    {
        // PREP - Create objects
        Query q0 = new Query(TestData.Q_NAMES[0], TestData.Q_DESCRIPTIONS[0], TestData.Q_QIDS[0], TestData.Q_TAGS[0]);
        Query q1 = new Query(TestData.Q_NAMES[1], TestData.Q_DESCRIPTIONS[1], TestData.Q_QIDS[1], TestData.Q_TAGS[1]);
        dao_query.add(q0);
        dao_query.add(q1);
        QueryCacheLine qcl0 = new QueryCacheLine(q0, TestData.QCL_RESULTS[0], TestData.QCL_RETRIEVED(0), TestData.QCL_PARAM_VALS[0]);
        QueryCacheLine qcl1 = new QueryCacheLine(q0, TestData.QCL_RESULTS[1], TestData.QCL_RETRIEVED(1), TestData.QCL_PARAM_VALS[1]);


        // ACT - Add
        dao.add(qcl0);
        dao.add(qcl1);

        // TEST - Add created object ID
        Assert.assertNotNull(qcl0.getId());

        // ACT - Get
        QueryCacheLine qcl0_copy = dao.get(qcl0.getId());

        // TEST - Get returned correct query and parameters
        Assert.assertEquals(qcl0.getId(), qcl0_copy.getId());
        Assert.assertEquals(qcl0.getParameterValueString(), qcl0_copy.getParameterValueString());
        Assert.assertEquals(qcl0.getQuery().getId(), qcl0_copy.getQuery().getId());
        Assert.assertEquals(qcl0.getResult(), qcl0_copy.getResult());
        Assert.assertEquals(qcl0.getRetrieved(), qcl0_copy.getRetrieved());

        // ACT - Update
        qcl1.setParameterValueString(TestData.QCL_PARAM_VALS[2]);
        qcl1.setQuery(q1);
        qcl1.setResult(TestData.QCL_RESULTS[2]);
        qcl1.setRetrieved(TestData.QCL_RETRIEVED(2));
        dao.update(qcl1);

        // TEST - Update seen in retrieved object
        QueryCacheLine qcl1_copy = dao.get(qcl1.getId());
        Assert.assertEquals(qcl1.getId(), qcl1_copy.getId());
        Assert.assertEquals(qcl1.getParameterValueString(), qcl1_copy.getParameterValueString());
        Assert.assertEquals(qcl1.getQuery().getId(), qcl1_copy.getQuery().getId());
        Assert.assertEquals(qcl1.getResult(), qcl1_copy.getResult());
        Assert.assertEquals(qcl1.getRetrieved(), qcl1_copy.getRetrieved());

        // ACT - Delete
        dao.delete(qcl0.getId());

        // TEST - Deleted
        Assert.assertEquals(1, dao.listAll().size());
    }
}