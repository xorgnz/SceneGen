package org.memehazard.wheel.activity.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.activity.dao.ActivityTemplateDAO;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Parameter;
import org.memehazard.wheel.activity.test.TestDAO_ActivityManager;
import org.memehazard.wheel.activity.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class ActivityTemplateDAOTest
{
    @Autowired
    private ActivityTemplateDAO     dao;

    @Autowired
    private TestDAO_ActivityManager dao_test;

    @SuppressWarnings("unused")
    private Logger                  log = LoggerFactory.getLogger(this.getClass());


    @Test
    @Transactional
    public void test_addGetUpdateDelete()
    {
        // PREP - Create test objects
        ActivityTemplate at0 = TestData.activityTemplate(0);
        Parameter atp0 = TestData.activityTemplateParameter(0);
        Parameter atp1 = TestData.activityTemplateParameter(1);
        Parameter atp2 = TestData.activityTemplateParameter(2);
        at0.addParameter(atp0);
        at0.addParameter(atp1);
        at0.addParameter(atp2);

        // ACT - Add objects
        dao.add(at0);
        dao.addParameters(at0);

        // TEST - Did we get object IDs?
        Assert.assertNotNull(at0.getId());

        // TEST - Can we retrieve object?
        ActivityTemplate at0_copy = dao.get(at0.getId());
        Assert.assertEquals(at0.getName(), at0_copy.getName());
        Assert.assertEquals(at0.getDescription(), at0_copy.getDescription());
        Assert.assertEquals(at0.getPlayUrl(), at0_copy.getPlayUrl());
        Assert.assertEquals(at0.getFactsUrl(), at0_copy.getFactsUrl());

        // TEST - Can we retrieve parameters? Are parameters ordered correctly
        Assert.assertEquals(3, at0_copy.getParameters().size());
        Assert.assertEquals(atp0.getLabel(), at0_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(atp0.getVariable(), at0_copy.getParameters().get(0).getVariable());
        Assert.assertEquals(atp0.getType(), at0_copy.getParameters().get(0).getType());
        Assert.assertEquals(atp1.getLabel(), at0_copy.getParameters().get(1).getLabel());
        Assert.assertEquals(atp1.getVariable(), at0_copy.getParameters().get(1).getVariable());
        Assert.assertEquals(atp1.getType(), at0_copy.getParameters().get(1).getType());
        Assert.assertEquals(atp2.getLabel(), at0_copy.getParameters().get(2).getLabel());
        Assert.assertEquals(atp2.getVariable(), at0_copy.getParameters().get(2).getVariable());
        Assert.assertEquals(atp2.getType(), at0_copy.getParameters().get(2).getType());

        // ACT - Update
        TestData.update(at0, 1);
        dao.update(at0);

        // TEST - Was update successful?
        at0_copy = dao.get(at0.getId());
        Assert.assertEquals(at0.getName(), at0_copy.getName());
        Assert.assertEquals(at0.getDescription(), at0_copy.getDescription());
        Assert.assertEquals(at0.getPlayUrl(), at0_copy.getPlayUrl());
        Assert.assertEquals(at0.getFactsUrl(), at0_copy.getFactsUrl());

        // ACT - Update parameters
        TestData.update(atp0, 3);
        TestData.update(atp1, 4);
        at0.removeParameter(atp2);
        dao.updateParameters(at0);

        // TEST - Was update successful?
        at0_copy = dao.get(at0.getId());
        Assert.assertEquals(2, at0_copy.getParameters().size());
        Assert.assertEquals(atp0.getLabel(), at0_copy.getParameters().get(0).getLabel());
        Assert.assertEquals(atp0.getVariable(), at0_copy.getParameters().get(0).getVariable());
        Assert.assertEquals(atp0.getType(), at0_copy.getParameters().get(0).getType());
        Assert.assertEquals(atp1.getLabel(), at0_copy.getParameters().get(1).getLabel());
        Assert.assertEquals(atp1.getVariable(), at0_copy.getParameters().get(1).getVariable());
        Assert.assertEquals(atp1.getType(), at0_copy.getParameters().get(1).getType());

        // ACT - Delete
        dao.delete(at0.getId());

        // TEST - Are all objects deleted?
        Assert.assertEquals(0, dao_test.countActivityTemplates());
        Assert.assertEquals(0, dao_test.countActivityTemplateParameters());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // PREP - Create test objects
        ActivityTemplate at0 = TestData.activityTemplate(0);
        ActivityTemplate at1 = TestData.activityTemplate(1);
        ActivityTemplate at2 = TestData.activityTemplate(2);
        dao.add(at2);
        dao.add(at1);
        dao.add(at0);

        // ACT
        List<ActivityTemplate> results = dao.listAll();

        // TEST - are all objects retrieved?
        Assert.assertEquals(3, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AT_NAME[0], results.get(0).getName());
        Assert.assertEquals(TestData.AT_NAME[1], results.get(1).getName());
        Assert.assertEquals(TestData.AT_NAME[2], results.get(2).getName());
    }
}
