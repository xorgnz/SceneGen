package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
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
public class CurriculumItemDAOTest
{
    // Components
    @Autowired
    private CurriculumItemDAO dao;
    @Autowired
    private CurriculumDAO     dao_c;
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
        // ACT - Add and retrieve curriculum
        CurriculumItem ci = dao.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci_copy = dao.get(ci.getNodeId());

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(1, dao.count());

        // TEST - Correct count of curricula in DB
        Assert.assertNotNull(ci_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(ci, ci_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(ci.getNodeId(), ci_copy.getNodeId());
        Assert.assertEquals(ci.getName(), ci_copy.getName());
        Assert.assertEquals(ci.getDescription(), ci_copy.getDescription());
        Assert.assertEquals(ci.getWeight(), ci_copy.getWeight());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        // ACT - Prepare test objects
        CurriculumItem ci = dao.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        dao.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        dao.add(new CurriculumItem(TestData.CI_NAME[2], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[2]));

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(3, dao.count());

        // ACT - Delete
        dao.delete(ci);

        // TEST - is retrieved curriculum same object as that added? Should fail
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
        Curriculum subj0 = dao_c.add(new Curriculum("Test", "test", "test"));

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
    public void testListAllByCurriculum()
    {
        // ACT - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum("Curriculum", "Creator name", "desc"));
        dao.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0],c));
        dao.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1],c));
        dao.add(new CurriculumItem(TestData.CI_NAME[2], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[2],c));

        // Retrieve list
        List<CurriculumItem> list = dao.listByCurriculum(c.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(3, list.size());

        // TEST - Curriculums returned in correct order
        Assert.assertEquals(TestData.CI_NAME[2], list.get(0).getName());
        Assert.assertEquals(TestData.CI_NAME[1], list.get(1).getName());
        Assert.assertEquals(TestData.CI_NAME[0], list.get(2).getName());
    }


    @Test
    @Transactional
    public void testListAllByCurriculum_NoItems()
    {
        log.trace(".testListAll_EmptyDB");

        // ACT - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum("Curriculum", "Creator name", "desc"));

        // ACT - Retrieve list
        List<CurriculumItem> list = dao.listByCurriculum(c.getNodeId());

        // TEST - List is empty
        Assert.assertEquals(0, list.size());
    }


    @Test
    @Transactional
    public void testUpdate()
    {
        // ACT - Prepare test objects
        Curriculum c1 = dao_c.add(new Curriculum("Curriculum 1", "Creator name", "desc"));
        Curriculum c2 = dao_c.add(new Curriculum("Curriculum 2", "Creator name", "desc"));
        CurriculumItem ci = dao.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c1));
        ci.setName(TestData.CI_NAME[1]);
        ci.setDescription(TestData.CI_DESCRIPTION);
        ci.setWeight(TestData.CI_WEIGHT[1]);
        ci.setCurriculum(c2);
        dao.update(ci);
        CurriculumItem ci_copy = dao.get(ci.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(ci_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(c1, ci_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(ci.getNodeId(), ci_copy.getNodeId());
        Assert.assertEquals(ci.getName(), ci_copy.getName());
        Assert.assertEquals(ci.getCurriculum(), ci_copy.getCurriculum());
        Assert.assertEquals(ci.getDescription(), ci_copy.getDescription());
        Assert.assertEquals(ci.getWeight(), ci_copy.getWeight());
    }
}
