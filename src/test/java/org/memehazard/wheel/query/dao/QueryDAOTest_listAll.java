package org.memehazard.wheel.query.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
import org.memehazard.wheel.query.test.TestDAO_Query;
import org.memehazard.wheel.query.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class QueryDAOTest_listAll
{
    @Autowired
    private QueryDAO      dao;

    @Autowired
    private TestDAO_Query dao_test;


    @Before
    public void before()
    {
        dao_test.clear();
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // PREP - Create objects
        Query q0 = new Query(TestData.Q_NAMES[0], TestData.Q_DESCRIPTIONS[0], TestData.Q_QIDS[0], TestData.Q_TAGS[0]);
        Query q1 = new Query(TestData.Q_NAMES[1], TestData.Q_DESCRIPTIONS[1], TestData.Q_QIDS[1], TestData.Q_TAGS[1]);
        Query q2 = new Query(TestData.Q_NAMES[2], TestData.Q_DESCRIPTIONS[2], TestData.Q_QIDS[2], TestData.Q_TAGS[2]);
        Query q3 = new Query(TestData.Q_NAMES[3], TestData.Q_DESCRIPTIONS[3], TestData.Q_QIDS[3], TestData.Q_TAGS[3]);
        dao.add(q0);
        dao.add(q1);
        dao.add(q2);
        dao.add(q3);
        QueryParameter qp0 = new QueryParameter(TestData.QP_VARIABLES[0], TestData.QP_LABELS[0], TestData.QP_TAGS(0));
        QueryParameter qp1 = new QueryParameter(TestData.QP_VARIABLES[1], TestData.QP_LABELS[1], TestData.QP_TAGS(1));
        QueryParameter qp2 = new QueryParameter(TestData.QP_VARIABLES[2], TestData.QP_LABELS[2], TestData.QP_TAGS(2));
        q3.addParameter(qp0);
        q3.addParameter(qp1);
        q3.addParameter(qp2);
        dao.addParameters(q3.getId(), q3.getParameters());

        // ACT = List All
        List<Query> queries = dao.listAll();

        // TEST - Correct number of queries returned
        Assert.assertEquals(4, queries.size());

        // TEST - Queries returned in correct order
        Query q0_copy = queries.get(3);
        Query q1_copy = queries.get(2);
        Query q2_copy = queries.get(1);
        Query q3_copy = queries.get(0);
        Assert.assertEquals(q0.getId(), q0_copy.getId());
        Assert.assertEquals(q1.getId(), q1_copy.getId());
        Assert.assertEquals(q2.getId(), q2_copy.getId());
        Assert.assertEquals(q3.getId(), q3_copy.getId());
        
        // TEST - Query parameters included
        Assert.assertEquals(3, q3_copy.getParameters().size());
        
        // TEST - Query parameters in correct order
        Assert.assertEquals(qp0.getLabel(), q3_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(qp1.getLabel(), q3_copy.getParameters().get(1).getLabel());
        Assert.assertEquals(qp2.getLabel(), q3_copy.getParameters().get(2).getLabel());
    }
}