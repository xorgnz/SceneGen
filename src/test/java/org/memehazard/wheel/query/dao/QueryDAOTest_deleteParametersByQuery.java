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
public class QueryDAOTest_deleteParametersByQuery
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
    public void test()
    {
        // PREP - Create objects
        Query q0 = new Query(TestData.Q_NAMES[0], TestData.Q_DESCRIPTIONS[0], TestData.Q_QIDS[0], TestData.Q_TAGS[0]);
        Query q1 = new Query(TestData.Q_NAMES[1], TestData.Q_DESCRIPTIONS[1], TestData.Q_QIDS[1], TestData.Q_TAGS[1]);
        dao.add(q0);
        dao.add(q1);                                                                                               
        QueryParameter qp0 = new QueryParameter(TestData.QP_VARIABLES[0], TestData.QP_LABELS[0], TestData.QP_TAGS(0));
        QueryParameter qp1 = new QueryParameter(TestData.QP_VARIABLES[1], TestData.QP_LABELS[1], TestData.QP_TAGS(1));
        QueryParameter qp2 = new QueryParameter(TestData.QP_VARIABLES[2], TestData.QP_LABELS[2], TestData.QP_TAGS(2));
        q0.addParameter(qp0);                                                                                      
        q0.addParameter(qp1);
        q1.addParameter(qp2);
        dao.addParameters(q0.getId(), q0.getParameters());
        dao.addParameters(q1.getId(), q1.getParameters());

        // ACT - deleteParametersByQuery
        dao.deleteParametersByQuery(q0.getId());

        // TEST - Correct parameters deleted
        Query q0_copy = dao.get(q0.getId());
        Query q1_copy = dao.get(q1.getId());
        Assert.assertEquals(0, q0_copy.getParameters().size());
        Assert.assertEquals(1, q1_copy.getParameters().size());
    }
}