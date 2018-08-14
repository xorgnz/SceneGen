package org.memehazard.wheel.tutoring.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.LabelFact;
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
public class LabelFactDAOTest
{
    @Autowired
    private LabelFactDAO       dao;
    // Components
    @Autowired
    private CurriculumDAO      dao_c;
    @Autowired
    private CurriculumItemDAO  dao_ci;
    @Autowired
    private EntityKnowledgeDAO dao_ek;
    @Autowired
    private LabelKnowledgeDAO  dao_rk;

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
        // ACT - Add and retrieve object
        EntityKnowledge ek = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        LabelKnowledge lk = dao_rk.add(new LabelKnowledge());
        LabelFact lfact = dao.add(new LabelFact(ek, lk));
        LabelFact lfact_copy = dao.get(lfact.getNodeId());

        // TEST - Correct count of objects in DB
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object
        Assert.assertNotNull(lfact_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(lfact, lfact_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(lfact.getNodeId(), lfact_copy.getNodeId());
        Assert.assertEquals(lfact.getEntityKnowledge(), lfact_copy.getEntityKnowledge());
        Assert.assertEquals(lfact.getLabelKnowledge(), lfact_copy.getLabelKnowledge());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        // PREP - Prepare test objects
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(1002, "EK 1002"));
        LabelKnowledge rk = dao_rk.add(new LabelKnowledge());
        LabelFact f00 = dao.add(new LabelFact(ek0, rk));
        LabelFact f10 = dao.add(new LabelFact(ek1, rk));
        LabelFact f20 = dao.add(new LabelFact(ek2, rk));

        // ACT - Delete
        dao.delete(f10);

        // TEST - do correct Facts remain?
        Assert.assertEquals(2, dao.count());
        Assert.assertNotNull(dao.get(f00.getNodeId()));
        Assert.assertNotNull(dao.get(f20.getNodeId()));
    }


    @Test
    @Transactional
    public void testDeleteMultiple()
    {
        // ACT - Prepare test objects
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(1002, "EK 1002"));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(1003, "EK 1003"));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(1003, "EK 1003"));
        LabelKnowledge rk = dao_rk.add(new LabelKnowledge());
        LabelFact f00 = dao.add(new LabelFact(ek0, rk));
        LabelFact f10 = dao.add(new LabelFact(ek1, rk));
        LabelFact f20 = dao.add(new LabelFact(ek2, rk));
        LabelFact f30 = dao.add(new LabelFact(ek3, rk));

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(4, dao.count());

        // ACT - Delete
        List<LabelFact> toDelete = new ArrayList<LabelFact>();
        toDelete.add(f10);
        toDelete.add(f30);
        dao.deleteMultiple(toDelete);

        // TEST - do correct Facts remain?
        Assert.assertEquals(2, dao.count());
        Assert.assertNotNull(dao.get(f00.getNodeId()));
        Assert.assertNotNull(dao.get(f20.getNodeId()));
    }


    @Test
    @Transactional
    public void testFindByEntityKnowledge()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci));
        LabelKnowledge lk0 = dao_rk.add(new LabelKnowledge(ci));
        LabelFact f00 = dao.add(new LabelFact(ek0, lk0));
        LabelFact f10 = dao.add(new LabelFact(ek1, lk0));

        // TEST - Label Facts are returned for EKs that have them
        Assert.assertNotNull(dao.findByEntityKnowledge(ek0.getNodeId()));
        Assert.assertNotNull(dao.findByEntityKnowledge(ek1.getNodeId()));
        Assert.assertNull(dao.findByEntityKnowledge(ek2.getNodeId()));

        // TEST - Retrieved facts have correct IDs
        Assert.assertEquals(f00.getNodeId(), dao.findByEntityKnowledge(ek0.getNodeId()).getNodeId());
        Assert.assertEquals(f10.getNodeId(), dao.findByEntityKnowledge(ek1.getNodeId()).getNodeId());
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
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));

        // TEST - Exception Thrown
        try
        {
            dao.get(ek0.getNodeId());
            Assert.fail("Should throw WrongNodeTypeException");
        }
        catch (WrongNodeTypeException e)
        {}
    }


    @Test
    @Transactional
    public void testListByCurriculumItem()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0],c));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        LabelKnowledge lk0 = dao_rk.add(new LabelKnowledge(ci));
        LabelKnowledge lk1 = dao_rk.add(new LabelKnowledge(null));
        LabelFact f00 = dao.add(new LabelFact(ek0, lk0));
        LabelFact f10 = dao.add(new LabelFact(ek1, lk0));
        LabelFact f01 = dao.add(new LabelFact(ek0, lk1));
        LabelFact f11 = dao.add(new LabelFact(ek1, lk1));

        // ACT - Retrieve list
        List<LabelFact> facts = dao.listByCurriculumItem(ci.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(facts);

        // TEST - Retrieve correct objects
        Assert.assertEquals(3, facts.size());
        Assert.assertTrue(facts.contains(f00)); // Entity, Label are linked
        Assert.assertTrue(facts.contains(f01)); // Entity is linked
        Assert.assertTrue(facts.contains(f10)); // Label is linked
        Assert.assertFalse(facts.contains(f11));
    }


    @Test
    @Transactional
    public void testListByLabelKnowledge()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0],c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1],c));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        LabelKnowledge lk0 = dao_rk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_rk.add(new LabelKnowledge(ci1));
        LabelFact f00 = dao.add(new LabelFact(ek00, lk0));
        LabelFact f10 = dao.add(new LabelFact(ek01, lk0));
        LabelFact f01 = dao.add(new LabelFact(ek10, lk1));
        LabelFact f11 = dao.add(new LabelFact(ek11, lk1));

        // ACT - Retrieve list
        List<LabelFact> facts = dao.listByLabelKnowledge(lk0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(facts);

        // TEST - Retrieve correct objects
        Assert.assertEquals(2, facts.size());
        Assert.assertTrue(facts.contains(f00));
        Assert.assertTrue(facts.contains(f10));
        Assert.assertFalse(facts.contains(f01));
        Assert.assertFalse(facts.contains(f11));
    }


    @Test
    @Transactional
    public void testUpdate()

    {
        // ACT - Prepare test objects
        EntityKnowledge ent0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        LabelKnowledge lk0 = dao_rk.add(new LabelKnowledge());
        EntityKnowledge ent1 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        LabelKnowledge lk1 = dao_rk.add(new LabelKnowledge());
        LabelFact fact = dao.add(new LabelFact(ent0, lk0));
        fact.setLabelKnowledge(lk1);
        fact.setEntityKnowledge(ent1);
        dao.update(fact);
        LabelFact fact_copy = dao.get(fact.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(fact_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(fact, fact_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(fact.getNodeId(), fact_copy.getNodeId());
        Assert.assertEquals(fact.getEntityKnowledge(), fact_copy.getEntityKnowledge());
        Assert.assertEquals(fact.getLabelKnowledge(), fact_copy.getLabelKnowledge());
    }
}
