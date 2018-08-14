package org.memehazard.wheel.tutoring.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.LabelKnowledge;
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
public class LabelKnowledgeDAOTest
{
    // Components
    @Autowired
    private LabelKnowledgeDAO  dao;
    @Autowired
    private CurriculumItemDAO  dao_ci;
    @Autowired
    private EntityKnowledgeDAO dao_ek;
    private Logger             log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Neo4jUtilities     utils;


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
        // ACT - Add and retrieve LK
        LabelKnowledge lk = dao.add(new LabelKnowledge());
        LabelKnowledge lk_copy = dao.get(lk.getNodeId());

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object
        Assert.assertNotNull(lk_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(lk, lk_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(lk.getNodeId(), lk_copy.getNodeId());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        // ACT - Prepare test objects
        LabelKnowledge lk = dao.add(new LabelKnowledge());
        dao.add(new LabelKnowledge());
        dao.add(new LabelKnowledge());

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(3, dao.count());

        // ACT - Delete
        dao.delete(lk);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertEquals(2, dao.count());
    }


    @Test
    @Transactional
    public void testFindByCurriculumItem()
    {
        // PREP - Create test objects
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        CurriculumItem ci2 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[2], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[2]));
        LabelKnowledge lk0 = dao.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao.add(new LabelKnowledge(ci1));

        // TEST - Label Knowledge nodes are returned for CIs that have them
        Assert.assertNotNull(dao.findByCurriculumItem(ci0.getNodeId()));
        Assert.assertNotNull(dao.findByCurriculumItem(ci1.getNodeId()));
        Assert.assertNull(dao.findByCurriculumItem(ci2.getNodeId()));

        // TEST - Retrieved facts have correct IDs
        Assert.assertEquals(lk0.getNodeId(), dao.findByCurriculumItem(ci0.getNodeId()).getNodeId());
        Assert.assertEquals(lk1.getNodeId(), dao.findByCurriculumItem(ci1.getNodeId()).getNodeId());
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
        {
        }
    }


    @Test
    @Transactional
    public void testGet_WrongNodeType()
    {
        log.trace(".testGet_WrongNodeType");

        // PREP - Create test object
        EntityKnowledge ek = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));

        // TEST - Exception Thrown
        try
        {
            dao.get(ek.getNodeId());
            Assert.fail("Should throw WrongNodeTypeException");
        }
        catch (WrongNodeTypeException e)
        {
        }
    }


    // @Test
    // @Transactional
    // public void testListAllByCurriculumItem()
    // {
    // // ACT - Prepare test objects
    // CurriculumItem ci = dao_ci.add(new CurriculumItem("Name", "Desc"));
    // dao.add(new LabelKnowledge(ci));
    // dao.add(new LabelKnowledge(ci));
    // dao.add(new LabelKnowledge(ci));
    // dao_ek.add(new EntityKnowledge(1001, "FMA Label", ci));
    //
    // // Retrieve list
    // List<LabelKnowledge> list = dao.listByCurriculumItem(ci.getNodeId());
    //
    // // TEST - List is not null
    // Assert.assertNotNull(list);
    //
    // // TEST - List contains correct number of objects
    // Assert.assertEquals(3, list.size());
    // }
    //
    //
    // @Test
    // @Transactional
    // public void testListAllByCurriculumItem_NoItems()
    // {
    // log.trace("Start testListAll_EmptyDB");
    //
    // // ACT - Prepare test objects
    // CurriculumItem ci = dao_ci.add(new CurriculumItem("Curriculum", "desc"));
    //
    // // ACT - Retrieve list
    // List<LabelKnowledge> list = dao.listByCurriculumItem(ci.getNodeId());
    //
    // // TEST - List is empty
    // Assert.assertEquals(0, list.size());
    //
    // log.trace("Finish testListAll_EmptyDB");
    // }


    @Test
    @Transactional
    public void testUpdate()
    {
        // ACT - Prepare test objects
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci2 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        LabelKnowledge rcsi = dao.add(new LabelKnowledge(ci1));
        rcsi.setCurriculumItem(ci2);
        dao.update(rcsi);
        LabelKnowledge rcsi_copy = dao.get(rcsi.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(rcsi_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(rcsi, rcsi_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(rcsi.getNodeId(), rcsi_copy.getNodeId());
        Assert.assertEquals(rcsi.getCurriculumItem(), rcsi_copy.getCurriculumItem());
    }
}
