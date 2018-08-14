package org.memehazard.wheel.tutoring.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.RelationFact;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
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
public class EntityKnowledgeDAOTest
{
    // Components
    @Autowired
    private EntityKnowledgeDAO   dao;
    @Autowired
    private CurriculumItemDAO    dao_ci;
    @Autowired
    private RelationFactDAO      dao_fact;
    @Autowired
    private RelationKnowledgeDAO dao_rk;
    private Logger               log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Neo4jUtilities       utils;


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
        log.trace(".testAddCountGet");

        // ACT - Add and retrieve curriculum
        EntityKnowledge ecsi = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]));
        EntityKnowledge ecsi_copy = dao.get(ecsi.getNodeId());

        // TEST - Correct object count
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object exists
        Assert.assertNotNull(ecsi_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(ecsi, ecsi_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(ecsi.getNodeId(), ecsi_copy.getNodeId());
        Assert.assertEquals(ecsi.getFmaId(), ecsi_copy.getFmaId());
        Assert.assertEquals(ecsi.getFmaLabel(), ecsi_copy.getFmaLabel());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        log.trace(".testDelete");

        // ACT - Prepare test objects
        EntityKnowledge ek = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1]));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2]));

        // TEST - Correct object count
        Assert.assertEquals(3, dao.count());

        // ACT - Delete
        dao.delete(ek);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertEquals(2, dao.count());
    }


    @Test
    @Transactional
    public void testDeleteMultiple()
    {
        log.trace(".testDeleteMultipleIfDetached");

        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));

        // ACT - Attempt deletion
        Set<EntityKnowledge> toDelete = new HashSet<EntityKnowledge>();
        toDelete.add(ek0);
        toDelete.add(ek1);
        dao.deleteMultiple(toDelete);

        // TEST - Correct object count
        Assert.assertEquals(1, dao.count());
    }


    @Test
    @Transactional
    public void testFindByCurriculumItemAndEntity()
    {
        log.trace(".testfindByCurriculumItemAndEntity");

        // ACT - Prepare test objects
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci0));

        // TEST - Match found for linked EK with same CI and FMAID
        Assert.assertNotNull(dao.findLinkedByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[0]));

        // TEST - No match found with different CI or FMAID
        Assert.assertNull(dao.findLinkedByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[0] + 1000));
        Assert.assertNull(dao.findLinkedByCurriculumItemAndEntity(ci1.getNodeId(), TestData.EK_FMA_ID[0]));

        // TEST - No match found with non-existent CI
        Assert.assertNull(dao.findLinkedByCurriculumItemAndEntity(ci0.getNodeId() + 10223, TestData.EK_FMA_ID[0]));
    }


    /**
     * Check:
     * - Return EK hanging as object
     * - Return EK hanging as subject
     * - Do not return linked EK
     * - Return EK only once when hanging by more than one fact
     * - Return nothing when CI does not match
     */
    @Test
    @Transactional
    public void testFindHangingByCurriculumItemAndEntity()
    {
        log.trace(".testFindHangingByCurriculumItemAndEntity");

        // ACT - Prepare test objects
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        EntityKnowledge ek0 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek1 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null)); // To
                                                                                                                   // hang
                                                                                                                   // as
                                                                                                                   // object
        EntityKnowledge ek2 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null)); // To
                                                                                                                   // hang
                                                                                                                   // as
                                                                                                                   // subject
        EntityKnowledge ek3 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], null)); // To
                                                                                                                   // doubly
                                                                                                                   // link
        EntityKnowledge ek4 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], ci0));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge("Name", "Namespace", ci0));
        dao_fact.add(new RelationFact(ek0, rk, ek1)); // EK hanging as object
        dao_fact.add(new RelationFact(ek2, rk, ek0)); // EK hanging as subject
        dao_fact.add(new RelationFact(ek0, rk, ek3)); // EK hanging via multiple facts
        dao_fact.add(new RelationFact(ek4, rk, ek3)); // EK hanging via multiple facts
        dao_fact.add(new RelationFact(ek0, rk, ek4)); // Both EKs linked

        // TEST - Returned EK hanging as object
        Assert.assertNotNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[1]));

        // TEST - Returned EK hanging as subject
        Assert.assertNotNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[2]));

        // TEST - Returned EK hanging via multiple facts
        Assert.assertNotNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[3]));

        // TEST - Did not return linked EK
        Assert.assertNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[0]));
        Assert.assertNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[4]));

        // TEST - No match found with different CI or FMAID
        Assert.assertNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId(), TestData.EK_FMA_ID[1] + 1000));
        Assert.assertNull(dao.findHangingByCurriculumItemAndEntity(ci1.getNodeId(), TestData.EK_FMA_ID[1]));

        // TEST - No match found with non-existent CI
        Assert.assertNull(dao.findHangingByCurriculumItemAndEntity(ci0.getNodeId() + 12223, TestData.EK_FMA_ID[1]));
    }


    @Test
    @Transactional
    public void testFindOrphanedInList()
    {
        log.trace(".testFindOrphanedInList");

        // PREP - Prepare Test Objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci)); // Linked
                                                                                                                 // to
                                                                                                                 // CI
        EntityKnowledge ek1 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null)); // Linked
                                                                                                                   // to
                                                                                                                   // fact
        EntityKnowledge ek2 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null)); // Linked
                                                                                                                   // to
                                                                                                                   // fact
        EntityKnowledge ek3 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], null)); // Detached
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        dao_fact.add(new RelationFact(ek1, rk, ek2));

        // ACT - ID detached EKs
        List<EntityKnowledge> eks = new ArrayList<EntityKnowledge>();
        eks.add(ek0);
        eks.add(ek1);
        eks.add(ek2);
        eks.add(ek3);
        List<EntityKnowledge> detachedEKs = dao.findOrphanedInList(eks);

        // TEST - correct EK returned.
        Assert.assertEquals(1, detachedEKs.size());
        Assert.assertEquals(ek3.getNodeId(), detachedEKs.get(0).getNodeId());
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
        RelationKnowledge subj0 = dao_rk.add(new RelationKnowledge("test", "test"));

        // TEST - Exception Thrown
        try
        {
            dao.get(subj0.getNodeId());
            Assert.fail("Should throw WrongNodeTypeException");
        }
        catch (WrongNodeTypeException e)
        {
        }
    }


    /**
     * Check
     * - Invocations return an empty list when no items are available to return
     */
    @Test
    @Transactional
    public void testListAllByCurriculumItem_NoItems()
    {
        log.trace(".testListAllByCurriculumItem_NoItems");

        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));

        // ACT - Retrieve list
        List<EntityKnowledge> list = dao.listLinkedByCurriculumItem(ci.getNodeId());

        // TEST - List is not null and empty
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());

        // ACT - Retrieve list with junk CI
        List<EntityKnowledge> list2 = dao.listHangingByCurriculumItem(ci.getNodeId() + 123545);

        // TEST - List is not null and empty
        Assert.assertNotNull(list2);
        Assert.assertEquals(0, list2.size());
    }


    /**
     * Check:
     * - EK hanging as object is returned
     * - EK hanging as subject is returned
     * - EK hanging via multiple facts is returned once
     * - Linked EKs not returned
     */
    @Test
    @Transactional
    public void testListHangingByCurriculumItem()
    {
        log.trace(".testListHangingByCurriculumItem");

        // PREP - Prepare test objects
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek1 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek2 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], null));
        EntityKnowledge ek4 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], null));
        EntityKnowledge ek5 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5], null));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge("Name", "Namespace", ci0));
        dao_fact.add(new RelationFact(ek0, rk, ek2)); // EK hanging as object
        dao_fact.add(new RelationFact(ek3, rk, ek0)); // EK hanging as subject
        dao_fact.add(new RelationFact(ek0, rk, ek4)); // EK hanging via multiple facts
        dao_fact.add(new RelationFact(ek1, rk, ek4)); // EK hanging via multiple facts

        // ACT - Retrieve list
        List<EntityKnowledge> eks = dao.listHangingByCurriculumItem(ci0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(eks);

        // TEST - Correct EKs returned
        Assert.assertEquals(3, eks.size());
        Assert.assertTrue(eks.contains(ek2));
        Assert.assertTrue(eks.contains(ek3));
        Assert.assertTrue(eks.contains(ek4));
        Assert.assertFalse(eks.contains(ek5));
    }


    /**
     * Check
     * - Invocations return an empty list when no items are available to return
     */
    @Test
    @Transactional
    public void testListHangingByCurriculumItem_NoItems()
    {
        log.trace(".testListHangingByCurriculumItem_NoItems");

        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));

        // ACT - Retrieve list
        List<EntityKnowledge> list = dao.listHangingByCurriculumItem(ci.getNodeId());

        // TEST - List is not null and empty
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());

        // ACT - Retrieve list with junk CI
        List<EntityKnowledge> list2 = dao.listHangingByCurriculumItem(ci.getNodeId() + 123545);

        // TEST - List is not null and empty
        Assert.assertNotNull(list2);
        Assert.assertEquals(0, list2.size());
    }


    /**
     * Check
     * - Linked EK acting as object is returned
     * - Linked EK acting as subject is returned
     * - Linked EK with no facts is returned
     * - Unlinked EK not returned
     */
    @Test
    @Transactional
    public void testListLinkedByCurriculumItem()
    {
        log.trace(".testListAllByCurriculumItem");

        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci)); // Acts
                                                                                                                 // as
                                                                                                                 // subject
        EntityKnowledge ek1 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci)); // Acts
                                                                                                                 // as
                                                                                                                 // object
        EntityKnowledge ek2 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci)); // No
                                                                                                                 // facts
        EntityKnowledge ek3 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], null)); // Unlinked
        EntityKnowledge ek4 = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], null)); // Unlinked,
                                                                                                                   // no
                                                                                                                   // facts
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge("Name", "Namespace", ci));
        dao_fact.add(new RelationFact(ek0, rk, ek3));
        dao_fact.add(new RelationFact(ek3, rk, ek1));

        // ACT - Retrieve list
        List<EntityKnowledge> list = dao.listLinkedByCurriculumItem(ci.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(3, list.size());
        Assert.assertTrue(list.contains(ek0));
        Assert.assertTrue(list.contains(ek1));
        Assert.assertTrue(list.contains(ek2));
        Assert.assertFalse(list.contains(ek3));
        Assert.assertFalse(list.contains(ek4));
    }


    @Test
    @Transactional
    public void testUpdate()
    {
        log.trace(".testUpdate");

        // ACT - Prepare test objects
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci2 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        EntityKnowledge ecsi = dao.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        ecsi.setFmaId(TestData.EK_FMA_ID[0] + 1000);
        ecsi.setFmaLabel(TestData.EK_FMA_LABEL + " alt");
        ecsi.setCurriculumItem(ci2);
        dao.update(ecsi);
        EntityKnowledge ecsi_copy = dao.get(ecsi.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(ecsi_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(ecsi, ecsi_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(ecsi.getNodeId(), ecsi_copy.getNodeId());
        Assert.assertEquals(ecsi.getCurriculumItem(), ecsi_copy.getCurriculumItem());
        Assert.assertEquals(ecsi.getFmaId(), ecsi_copy.getFmaId());
        Assert.assertEquals(ecsi.getFmaLabel(), ecsi_copy.getFmaLabel());
    }
}
