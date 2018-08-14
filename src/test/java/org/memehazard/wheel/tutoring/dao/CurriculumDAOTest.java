package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.SimpleTestNodeDAO;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.test.TestData;
import org.neo4j.graphdb.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class CurriculumDAOTest
{
    // Components
    @Autowired
    private CurriculumDAO     dao;
    @Autowired
    private CurriculumItemDAO dao_ci;
    @Autowired
    private SimpleTestNodeDAO dao_stn;
    private Logger            log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Neo4jUtilities    utils;


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
    public void testAddCountGet()
    {
        log.info(".testAdd");

        // ACT - Prepare test objects
        Curriculum c1 = dao.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        Curriculum c1_copy = dao.get(c1.getNodeId());

        // TEST - DB contains one Curriculum
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(c1_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(c1, c1_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(c1.getNodeId(), c1_copy.getNodeId());
        Assert.assertEquals(c1.getName(), c1_copy.getName());
        Assert.assertEquals(c1.getCreatorName(), c1_copy.getCreatorName());
        Assert.assertEquals(c1.getDescription(), c1_copy.getDescription());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        log.info(".testDelete");

        // ACT - Prepare test objects
        Curriculum c = dao.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        dao.add(new Curriculum(TestData.C_NAME[1], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        dao.add(new Curriculum(TestData.C_NAME[2], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));

        // TEST - DB contains correct number of Curriculums
        Assert.assertEquals(3, dao.count());

        // ACT - Delete one object
        dao.delete(c);

        // TEST - DB contains correct number of Curriculums
        Assert.assertEquals(2, dao.count());
    }


    @Test
    @Transactional
    public void testGet_NotFound()
    {
        log.trace(".testGet_NotFound");

        // TEST - Exception Thrown
        try
        {
            dao.get(993L);
            Assert.fail("Should throw NotFoundException");
        }
        catch (NotFoundException e)
        {}
    }


    @Test
    @Transactional
    public void testGet_WrongNodeType()
    {
        log.trace(".testGet_WrongNodeType");

        // PREP - Create test object
        CurriculumItem subj0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));

        // TEST - Exception Thrown
        try
        {
            dao.get(subj0.getNodeId());
            Assert.fail("Should throw WrongNodeTypeException");
        }
        catch (WrongNodeTypeException e)
        {}
    }


    @Test
    @Transactional
    public void testListAll()
    {
        log.info(".testListAll");

        // ACT - Prepare test objects
        dao.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        dao.add(new Curriculum(TestData.C_NAME[1], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        dao.add(new Curriculum(TestData.C_NAME[2], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        List<Curriculum> result = dao.listAll();

        // TEST - List is not null
        Assert.assertNotNull(result);

        // TEST - Correct number of Curriculums returned
        Assert.assertEquals(3, result.size());

        // TEST - Curriculums returned in correct order
        Assert.assertEquals(TestData.C_NAME[2], result.get(0).getName());
        Assert.assertEquals(TestData.C_NAME[1], result.get(1).getName());
        Assert.assertEquals(TestData.C_NAME[0], result.get(2).getName());
    }


    @Test
    @Transactional
    public void testListAll_EmptyDB()
    {
        log.info(".testListAll_EmptyDB");

        // ACT - Retrieve list of Curriculums
        List<Curriculum> result = dao.listAll();

        // TEST - List is empty
        Assert.assertEquals(0, result.size());
    }


    public void testUpdate()
    {
        log.info(".testUpdate");

        // ACT - Prepare test objects
        Curriculum c1 = dao.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        c1.setName(TestData.C_NAME[0]);
        c1.setCreatorName(TestData.C_CREATOR_NAME);
        c1.setDescription(TestData.C_DESCRIPTION);
        dao.update(c1);
        Curriculum c1_copy = dao.get(c1.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(c1_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(c1, c1_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(c1.getNodeId(), c1_copy.getNodeId());
        Assert.assertEquals(c1.getName(), c1_copy.getName());
        Assert.assertEquals(c1.getCreatorName(), c1_copy.getCreatorName());
        Assert.assertEquals(c1.getDescription(), c1_copy.getDescription());
    }
}
