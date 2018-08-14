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
public class QueryDAOTest_crud
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
        QueryParameter qp0 = new QueryParameter(TestData.QP_VARIABLES[0], TestData.QP_LABELS[0], TestData.QP_TAGS(0));
        QueryParameter qp1 = new QueryParameter(TestData.QP_VARIABLES[1], TestData.QP_LABELS[1], TestData.QP_TAGS(1));
        q0.addParameter(qp0);
        q1.addParameter(qp1);

        // ACT - Add
        dao.add(q0);
        dao.add(q1);
        dao.addParameters(q0.getId(), q0.getParameters());

        // TEST - Add created object ID
        Assert.assertNotNull(q0.getId());

        // ACT - Get
        Query q0_copy = dao.get(q0.getId());

        // TEST - Get returned correct query and parameters
        Assert.assertEquals(q0.getName(), q0_copy.getName());
        Assert.assertEquals(q0.getDescription(), q0_copy.getDescription());
        Assert.assertEquals(q0.getQueryId(), q0_copy.getQueryId());
        Assert.assertEquals(q0.getTags(), q0_copy.getTags());
        Assert.assertEquals(q0.getParameters().size(), q0_copy.getParameters().size());
        Assert.assertEquals(qp0.getVariable(), q0_copy.getParameters().get(0).getVariable());
        Assert.assertEquals(qp0.getLabel(), q0_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(qp0.getTagString(), q0_copy.getParameters().get(0).getTagString());

        // ACT - Update
        q1.setName(TestData.Q_NAMES[2]);
        q1.setDescription(TestData.Q_DESCRIPTIONS[2]);
        q1.setQueryId(TestData.Q_QIDS[2]);
        q1.setTags(TestData.Q_TAGS[2]);
        qp1.setLabel(TestData.QP_LABELS[2]);
        qp1.setVariable(TestData.QP_VARIABLES[2]);
        qp1.setTags(TestData.QP_TAGS(2));
        dao.update(q1);
        dao.updateParameters(q1.getId(), q1.getParameters());

        // TEST - Update seen in retrieved object
        Query q1_copy = dao.get(q1.getId());
        Assert.assertEquals(q1.getName(), q1_copy.getName());
        Assert.assertEquals(q1.getDescription(), q1_copy.getDescription());
        Assert.assertEquals(q1.getQueryId(), q1_copy.getQueryId());
        Assert.assertEquals(q1.getTags(), q1_copy.getTags());
        Assert.assertEquals(q1.getParameters().size(), q1_copy.getParameters().size());
        Assert.assertEquals(qp1.getVariable(), q1_copy.getParameters().get(0).getVariable());
        Assert.assertEquals(qp1.getLabel(), q1_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(qp1.getTagString(), q1_copy.getParameters().get(0).getTagString());

        // ACT - Delete
        dao.delete(q0.getId());

        // TEST - Deleted
        Assert.assertEquals(1, dao.listAll().size());
    }
}