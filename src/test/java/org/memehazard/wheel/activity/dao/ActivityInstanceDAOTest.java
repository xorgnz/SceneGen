package org.memehazard.wheel.activity.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.activity.model.ActivityInstance;
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
public class ActivityInstanceDAOTest
{
    @Autowired
    private ActivityInstanceDAO     dao;

    @Autowired
    private ActivityTemplateDAO     dao_at;

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
        ActivityTemplate at1 = TestData.activityTemplate(1);
        Parameter atp0 = TestData.activityTemplateParameter(0);
        Parameter atp1 = TestData.activityTemplateParameter(1);
        Parameter atp2 = TestData.activityTemplateParameter(2);
        Parameter atp3 = TestData.activityTemplateParameter(3);
        Parameter atp4 = TestData.activityTemplateParameter(4);
        at0.addParameter(atp0);
        at0.addParameter(atp1);
        at0.addParameter(atp2);
        at1.addParameter(atp3);
        at1.addParameter(atp4);
        dao_at.add(at0);
        dao_at.addParameters(at0);
        dao_at.add(at1);
        dao_at.addParameters(at1);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);

        // ACT - Add objects
        dao.add(ai0);

        // TEST - Did we get an object ID?
        Assert.assertNotNull(ai0.getId());

        // TEST - Can we retrieve object?
        ActivityInstance ai0_copy = dao.get(ai0.getId());
        Assert.assertEquals(ai0.getName(), ai0_copy.getName());
        Assert.assertEquals(ai0.getDescription(), ai0_copy.getDescription());
        Assert.assertEquals(ai0.getParameterValueString(), ai0_copy.getParameterValueString());
        Assert.assertEquals(ai0.getTemplate().getId(), ai0_copy.getTemplate().getId());

        // ACT - Update
        TestData.update(ai0, 1, at1);
        dao.update(ai0);

        // TEST - Was update successful?
        ai0_copy = dao.get(ai0.getId());
        Assert.assertEquals(ai0.getName(), ai0_copy.getName());
        Assert.assertEquals(ai0.getDescription(), ai0_copy.getDescription());
        Assert.assertEquals(ai0.getParameterValueString(), ai0_copy.getParameterValueString());
        Assert.assertEquals(ai0.getTemplate().getId(), ai0_copy.getTemplate().getId());

        // ACT - Delete
        dao.delete(ai0.getId());

        // TEST - Are all objects deleted?
        Assert.assertEquals(2, dao_test.countActivityTemplates());
        Assert.assertEquals(5, dao_test.countActivityTemplateParameters());
        Assert.assertEquals(0, dao_test.countActivityInstances());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // PREP - Create test objects
        ActivityTemplate at0 = TestData.activityTemplate(0);
        dao_at.add(at0);
        Parameter atp0 = TestData.activityTemplateParameter(0);
        at0.addParameter(atp0);
        dao_at.addParameters(at0);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);
        ActivityInstance ai1 = TestData.activityInstance(1, at0);
        ActivityInstance ai2 = TestData.activityInstance(2, at0);
        dao.add(ai0);
        dao.add(ai1);
        dao.add(ai2);

        // ACT
        List<ActivityInstance> results = dao.listAll();

        // TEST - are all objects retrieved?
        Assert.assertEquals(3, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AI_NAME[0], results.get(0).getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(1).getName());
        Assert.assertEquals(TestData.AI_NAME[2], results.get(2).getName());
    }


    @Test
    @Transactional
    public void test_listByTemplate()
    {
        // PREP - Create test objects
        ActivityTemplate at0 = TestData.activityTemplate(0);
        dao_at.add(at0);
        ActivityTemplate at1 = TestData.activityTemplate(0);
        dao_at.add(at1);
        Parameter atp0 = TestData.activityTemplateParameter(0);
        at0.addParameter(atp0);
        dao_at.addParameters(at0);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);
        ActivityInstance ai1 = TestData.activityInstance(1, at0);
        ActivityInstance ai2 = TestData.activityInstance(2, at0);
        ActivityInstance ai3 = TestData.activityInstance(3, at1);
        ActivityInstance ai4 = TestData.activityInstance(4, at1);
        ActivityInstance ai5 = TestData.activityInstance(5, at1);
        dao.add(ai0);
        dao.add(ai1);
        dao.add(ai2);
        dao.add(ai3);
        dao.add(ai4);
        dao.add(ai5);

        // ACT
        List<ActivityInstance> results = dao.listByTemplate(at0.getId());

        // TEST - are all objects retrieved?
        Assert.assertEquals(3, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AI_NAME[0], results.get(0).getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(1).getName());
        Assert.assertEquals(TestData.AI_NAME[2], results.get(2).getName());
    }
}
