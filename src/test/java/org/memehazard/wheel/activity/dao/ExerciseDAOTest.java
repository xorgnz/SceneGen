package org.memehazard.wheel.activity.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.activity.test.TestDAO_ActivityManager;
import org.memehazard.wheel.activity.test.TestData;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class ExerciseDAOTest
{
    @Autowired
    private ActivityInstanceDAO     dao_ai;

    @Autowired
    private ExerciseDAO             dao;

    @Autowired
    private CurriculumDAO           dao_c;

    @Autowired
    private ActivityTemplateDAO     dao_at;

    @Autowired
    private TestDAO_ActivityManager dao_test;

    private Logger                  log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Neo4jUtilities          utils;


    /**
     * Before each transactional test, clear the database and prepare test objects
     */
    @BeforeTransaction
    public void beforeTransaction()
    {
        log.trace("------------------- BEFORE TX");

        // ACT - Ensure the database is cleared
        utils.clearDatabase();
    }


    @Test
    @Transactional
    public void test_addListAllDelete()
    {
        // PREP - Create test objects
        Curriculum c0 = TestData.curriculum(0);
        Curriculum c1 = TestData.curriculum(0);
        dao_c.add(c0);
        dao_c.add(c1);
        ActivityTemplate at0 = TestData.activityTemplate(0);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);
        ActivityInstance ai1 = TestData.activityInstance(1, at0);
        dao_at.add(at0);
        dao_ai.add(ai0);
        dao_ai.add(ai1);
        Exercise ex0 = new Exercise(ai0, c0);
        Exercise ex1 = new Exercise(ai0, c1);
        Exercise ex2 = new Exercise(ai1, c0);
        Exercise ex3 = new Exercise(ai1, c1);
        dao.add(ex0);
        dao.add(ex1);
        dao.add(ex2);
        dao.add(ex3);

        // TEST - Did we get object IDs?
        Assert.assertNotNull(ex0.getId());
        Assert.assertNotNull(ex1.getId());
        Assert.assertNotNull(ex2.getId());
        Assert.assertNotNull(ex3.getId());

        // ACT - List All
        List<Exercise> results = dao.listAll();

        // TEST - are all objects retrieved?
        Assert.assertEquals(4, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AI_NAME[0], results.get(0).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[0], results.get(1).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(2).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(2).getActivityInstance().getName());

        // ACT - Delete
        dao.delete(ex0.getId());
        dao.delete(ex1.getId());

        // ACT - List All
        results = dao.listAll();

        // TEST - are all objects retrieved?
        Assert.assertEquals(2, results.size());
    }


    @Test
    @Transactional
    public void test_listByActivityInstance()
    {
        // PREP - Create test objects
        Curriculum c0 = TestData.curriculum(0);
        Curriculum c1 = TestData.curriculum(1);
        Curriculum c2 = TestData.curriculum(2);
        dao_c.add(c0);
        dao_c.add(c1);
        dao_c.add(c2);
        ActivityTemplate at0 = TestData.activityTemplate(0);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);
        ActivityInstance ai1 = TestData.activityInstance(1, at0);
        ActivityInstance ai2 = TestData.activityInstance(2, at0);
        dao_at.add(at0);
        dao_ai.add(ai0);
        dao_ai.add(ai1);
        dao_ai.add(ai2);
        Exercise ex0 = new Exercise(ai0, c0);
        Exercise ex1 = new Exercise(ai0, c1);
        Exercise ex2 = new Exercise(ai0, c2);
        Exercise ex3 = new Exercise(ai1, c0);
        Exercise ex4 = new Exercise(ai2, c0);
        dao.add(ex0);
        dao.add(ex1);
        dao.add(ex2);
        dao.add(ex3);
        dao.add(ex4);

        // ACT
        List<Exercise> results = dao.listByCurriculum(c0.getNodeId());

        // TEST - are all objects retrieved?
        Assert.assertEquals(3, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AI_NAME[0], results.get(0).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(1).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[2], results.get(2).getActivityInstance().getName());
    }


    @Test
    @Transactional
    public void test_listByCurriculum()
    {
        // PREP - Create test objects
        Curriculum c0 = TestData.curriculum(0);
        Curriculum c1 = TestData.curriculum(1);
        Curriculum c2 = TestData.curriculum(2);
        dao_c.add(c0);
        dao_c.add(c1);
        dao_c.add(c2);
        ActivityTemplate at0 = TestData.activityTemplate(0);
        ActivityInstance ai0 = TestData.activityInstance(0, at0);
        ActivityInstance ai1 = TestData.activityInstance(1, at0);
        ActivityInstance ai2 = TestData.activityInstance(2, at0);
        dao_at.add(at0);
        dao_ai.add(ai0);
        dao_ai.add(ai1);
        dao_ai.add(ai2);
        Exercise ex0 = new Exercise(ai0, c0);
        Exercise ex1 = new Exercise(ai1, c0);
        Exercise ex2 = new Exercise(ai2, c0);
        Exercise ex3 = new Exercise(ai0, c1);
        Exercise ex4 = new Exercise(ai0, c2);
        dao.add(ex0);
        dao.add(ex1);
        dao.add(ex2);
        dao.add(ex3);
        dao.add(ex4);

        // ACT
        List<Exercise> results = dao.listByCurriculum(c0.getNodeId());

        // TEST - are all objects retrieved?
        Assert.assertEquals(3, results.size());

        // TEST - are objects ordered by name?
        Assert.assertEquals(TestData.AI_NAME[0], results.get(0).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[1], results.get(1).getActivityInstance().getName());
        Assert.assertEquals(TestData.AI_NAME[2], results.get(2).getActivityInstance().getName());
    }
}
