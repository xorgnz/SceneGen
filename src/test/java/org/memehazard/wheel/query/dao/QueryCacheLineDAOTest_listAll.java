package org.memehazard.wheel.query.dao;

import java.util.List;

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
public class QueryCacheLineDAOTest_listAll
{
    @Autowired
    private QueryDAO          dao_query;

    @Autowired
    private QueryCacheLineDAO dao;

    @Autowired
    private TestDAO_Query     dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    public void test_listAll()
    {
        // PREP - Create objects
        Query q0 = new Query(TestData.Q_NAMES[0], TestData.Q_DESCRIPTIONS[0], TestData.Q_QIDS[0], TestData.Q_TAGS[0]);
        Query q1 = new Query(TestData.Q_NAMES[1], TestData.Q_DESCRIPTIONS[1], TestData.Q_QIDS[1], TestData.Q_TAGS[1]);
        dao_query.add(q0);
        dao_query.add(q1);
        QueryCacheLine qcl0 = new QueryCacheLine(q0, TestData.QCL_RESULTS[0], TestData.QCL_RETRIEVED(0), TestData.QCL_PARAM_VALS[0]);
        QueryCacheLine qcl1 = new QueryCacheLine(q0, TestData.QCL_RESULTS[1], TestData.QCL_RETRIEVED(1), TestData.QCL_PARAM_VALS[1]);
        QueryCacheLine qcl2 = new QueryCacheLine(q0, TestData.QCL_RESULTS[2], TestData.QCL_RETRIEVED(2), TestData.QCL_PARAM_VALS[2]);
        QueryCacheLine qcl3 = new QueryCacheLine(q1, TestData.QCL_RESULTS[3], TestData.QCL_RETRIEVED(3), TestData.QCL_PARAM_VALS[3]);
        QueryCacheLine qcl4 = new QueryCacheLine(q1, TestData.QCL_RESULTS[4], TestData.QCL_RETRIEVED(4), TestData.QCL_PARAM_VALS[4]);
        QueryCacheLine qcl5 = new QueryCacheLine(q1, TestData.QCL_RESULTS[5], TestData.QCL_RETRIEVED(5), TestData.QCL_PARAM_VALS[5]);
        dao.add(qcl0);
        dao.add(qcl1);
        dao.add(qcl2);
        dao.add(qcl3);
        dao.add(qcl4);
        dao.add(qcl5);

        // Act
        List<QueryCacheLine> qcls = dao.listAll();

        // Test - are all QCLs retrieved?
        Assert.assertEquals(6, qcls.size());

        // Test - are QCLs ordered by retrieval date?
        Assert.assertEquals(qcl0.getParameterValueString(), qcls.get(5).getParameterValueString());
        Assert.assertEquals(qcl1.getParameterValueString(), qcls.get(4).getParameterValueString());
        Assert.assertEquals(qcl2.getParameterValueString(), qcls.get(3).getParameterValueString());
        Assert.assertEquals(qcl3.getParameterValueString(), qcls.get(2).getParameterValueString());
        Assert.assertEquals(qcl4.getParameterValueString(), qcls.get(1).getParameterValueString());
        Assert.assertEquals(qcl5.getParameterValueString(), qcls.get(0).getParameterValueString());
    }
}