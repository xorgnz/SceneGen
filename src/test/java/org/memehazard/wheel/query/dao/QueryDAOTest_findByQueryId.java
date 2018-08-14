package org.memehazard.wheel.query.dao;

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
public class QueryDAOTest_findByQueryId
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
    public void test_findByQueryId()
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
        q2.addParameter(qp0);
        q2.addParameter(qp1);
        q2.addParameter(qp2);
        dao.addParameters(q2.getId(), q2.getParameters());

        // ACT
        Query q2_copy = dao.findByQueryId(q2.getQueryId());

        // Test - did we get an object back?
        Assert.assertNotNull(q2_copy);

        // Test - does that object have expected values?
        Assert.assertEquals(q2.getId(), q2_copy.getId());
        Assert.assertEquals(q2.getName(), q2_copy.getName());
        Assert.assertEquals(q2.getDescription(), q2_copy.getDescription());
        Assert.assertEquals(q2.getQueryId(), q2_copy.getQueryId());
        Assert.assertEquals(q2.getTags(), q2_copy.getTags());

        // Test - are the correct number of parameters associated?
        Assert.assertEquals(3, q2_copy.getParameters().size());

        // Test - are associated parameters in the correct order?
        Assert.assertEquals(qp0.getLabel(), q2_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(qp1.getLabel(), q2_copy.getParameters().get(1).getLabel());
        Assert.assertEquals(qp2.getLabel(), q2_copy.getParameters().get(2).getLabel());
    }
}