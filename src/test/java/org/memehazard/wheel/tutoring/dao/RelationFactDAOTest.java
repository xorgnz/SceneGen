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
public class RelationFactDAOTest
{
    @Autowired
    private RelationFactDAO      dao;
    // Components
    @Autowired
    private CurriculumDAO        dao_c;
    @Autowired
    private CurriculumItemDAO    dao_ci;
    @Autowired
    private EntityKnowledgeDAO   dao_ek;
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
        // ACT - Add and retrieve object
        EntityKnowledge subj = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge obj = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        RelationKnowledge rel = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact fact = dao.add(new RelationFact(subj, rel, obj));
        RelationFact fact_copy = dao.get(fact.getNodeId());

        // TEST - Correct count of objects in DB
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object
        Assert.assertNotNull(fact_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(fact, fact_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(fact.getNodeId(), fact_copy.getNodeId());
        Assert.assertEquals(fact.getSubject(), fact_copy.getSubject());
        Assert.assertEquals(fact.getObject(), fact_copy.getObject());
        Assert.assertEquals(fact.getRelation(), fact_copy.getRelation());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        // PREP - Prepare test objects
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(1002, "EK 1002"));
        RelationKnowledge rel = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact f01 = dao.add(new RelationFact(ek0, rel, ek1));
        RelationFact f02 = dao.add(new RelationFact(ek0, rel, ek2));
        RelationFact f12 = dao.add(new RelationFact(ek1, rel, ek2));

        // ACT - Delete
        dao.delete(f01);

        // TEST - do correct Facts remain?
        Assert.assertEquals(2, dao.count());
        Assert.assertNotNull(dao.get(f02.getNodeId()));
        Assert.assertNotNull(dao.get(f12.getNodeId()));
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
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact f01 = dao.add(new RelationFact(ek0, rk, ek1));
        RelationFact f02 = dao.add(new RelationFact(ek0, rk, ek2));
        RelationFact f03 = dao.add(new RelationFact(ek0, rk, ek3));
        RelationFact f12 = dao.add(new RelationFact(ek1, rk, ek2));
        RelationFact f13 = dao.add(new RelationFact(ek1, rk, ek3));
        RelationFact f23 = dao.add(new RelationFact(ek2, rk, ek3));

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(6, dao.count());

        // ACT - Delete
        List<RelationFact> toDelete = new ArrayList<RelationFact>();
        toDelete.add(f01);
        toDelete.add(f02);
        toDelete.add(f03);
        dao.deleteMultiple(toDelete);

        // TEST - do correct Facts remain?
        Assert.assertEquals(3, dao.count());
        Assert.assertNotNull(dao.get(f12.getNodeId()));
        Assert.assertNotNull(dao.get(f13.getNodeId()));
        Assert.assertNotNull(dao.get(f23.getNodeId()));
    }


    @Test
    @Transactional
    public void testFind()
    {
        // PREP - Test Objects
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1000, "EK 1001"));
        EntityKnowledge obj0 = dao_ek.add(new EntityKnowledge(1001, "EK 2000"));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(1001, "EK 2001"));
        RelationKnowledge rel0 = dao_rk.add(new RelationKnowledge("Name0", "Namespace0"));
        RelationKnowledge rel1 = dao_rk.add(new RelationKnowledge("Name1", "Namespace1"));
        dao.add(new RelationFact(subj0, rel0, obj0));

        // ACT - Find test object
        RelationFact f_copy = dao.find(subj0.getNodeId(), rel0.getNodeId(), obj0.getNodeId());

        // TEST - Correctly found test object
        Assert.assertNotNull("Cannot find test object", f_copy);
        Assert.assertEquals(subj0.getNodeId(), f_copy.getSubject().getNodeId());
        Assert.assertEquals(rel0.getNodeId(), f_copy.getRelation().getNodeId());
        Assert.assertEquals(obj0.getNodeId(), f_copy.getObject().getNodeId());

        // TEST - Cannot find non-existent test objects
        Assert.assertNull(dao.find(subj1.getNodeId(), rel0.getNodeId(), obj0.getNodeId()));
        Assert.assertNull(dao.find(subj0.getNodeId(), rel1.getNodeId(), obj0.getNodeId()));
        Assert.assertNull(dao.find(subj0.getNodeId(), rel0.getNodeId(), obj1.getNodeId()));
        Assert.assertNull(dao.find(subj1.getNodeId(), rel1.getNodeId(), obj1.getNodeId()));
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
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));

        // TEST - Exception Thrown
        try
        {
            dao.get(subj0.getNodeId());
            Assert.fail("Should throw WrongNodeTypeException");
        }
        catch (WrongNodeTypeException e)
        {}
    }


    /**
     * Check:
     * - Facts where target EK node is subject are returned
     * - Facts where target EK node is object are returned
     * - Empty list is returned when target EK node has no associated facts
     */
    @Test
    @Transactional
    public void testListByAnyEntity()
    {
        // ACT - Prepare test objects
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(1002, "EK 1002"));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(1003, "EK 1003"));
        RelationKnowledge rel = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact f01 = dao.add(new RelationFact(ek0, rel, ek1));
        RelationFact f12 = dao.add(new RelationFact(ek1, rel, ek2));
        RelationFact f20 = dao.add(new RelationFact(ek2, rel, ek0));

        // ACT - Retrieve list of facts
        List<RelationFact> facts = dao.listByAnyEntity(ek0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(facts);

        // TEST - Retrieved correct objects
        Assert.assertEquals(2, facts.size());
        Assert.assertTrue(facts.contains(f01));
        Assert.assertFalse(facts.contains(f12));
        Assert.assertTrue(facts.contains(f20));

        // ACT - Retrieve empty list of facts
        facts = dao.listByAnyEntity(ek3.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(facts);

        // TEST - List contains correct number of objects
        Assert.assertEquals(0, facts.size());
    }


    /**
     * Check:
     * - Facts where subject is linked are returned
     * - Facts where object is linked are returned
     * - Facts where only relation is linked are returned (this should never occur)
     * - Facts where relation is not linked are returned (this should never occur)
     */
    @Test
    @Transactional
    public void testListByCurriculumItem()
    {
        // ACT - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum("name", "creator", "desc"));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "SUBJ 0", ci));
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1001, "SUBJ 1", null));
        EntityKnowledge obj0 = dao_ek.add(new EntityKnowledge(2000, "OBJ 0", ci));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(2001, "OBJ 1", null));
        RelationKnowledge rel0 = dao_rk.add(new RelationKnowledge("Name", "Namespace", ci));
        RelationKnowledge rel1 = dao_rk.add(new RelationKnowledge("Name", "Namespace", null));
        RelationFact f000 = dao.add(new RelationFact(subj0, rel0, obj0));
        RelationFact f001 = dao.add(new RelationFact(subj0, rel0, obj1));
        RelationFact f100 = dao.add(new RelationFact(subj1, rel0, obj0));
        RelationFact f101 = dao.add(new RelationFact(subj1, rel0, obj1));
        RelationFact f010 = dao.add(new RelationFact(subj0, rel1, obj0));
        RelationFact f011 = dao.add(new RelationFact(subj0, rel1, obj1));
        RelationFact f110 = dao.add(new RelationFact(subj1, rel1, obj0));
        RelationFact f111 = dao.add(new RelationFact(subj1, rel1, obj1));

        // ACT - Retrieve list
        List<RelationFact> facts = dao.listByCurriculumItem(ci.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(facts);

        // TEST - Retrieve correct objects
        Assert.assertEquals(7, facts.size());
        Assert.assertTrue(facts.contains(f000)); // Subject, Relation, Object are linked
        Assert.assertTrue(facts.contains(f001)); // Subject, Relation are linked
        Assert.assertTrue(facts.contains(f100)); // Relation, Object are linked
        Assert.assertTrue(facts.contains(f101)); // Relation is linked
        Assert.assertTrue(facts.contains(f010)); // Subject, Object are linked
        Assert.assertTrue(facts.contains(f011)); // Subject is linked
        Assert.assertTrue(facts.contains(f110)); // Object is linked
        Assert.assertFalse(facts.contains(f111));
    }


    @Test
    @Transactional
    public void testListByObjectEntity()
    {
        // ACT - Prepare test objects
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "SUBJ 0"));
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1001, "SUBJ 1"));
        EntityKnowledge subj2 = dao_ek.add(new EntityKnowledge(1002, "SUBJ 2"));
        EntityKnowledge obj0 = dao_ek.add(new EntityKnowledge(2000, "OBJ 0"));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(2001, "OBJ 1"));
        RelationKnowledge rel = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact f00 = dao.add(new RelationFact(subj0, rel, obj0));
        RelationFact f01 = dao.add(new RelationFact(subj0, rel, obj1));
        RelationFact f10 = dao.add(new RelationFact(subj1, rel, obj0));
        RelationFact f11 = dao.add(new RelationFact(subj1, rel, obj1));
        RelationFact f20 = dao.add(new RelationFact(subj2, rel, obj0));
        RelationFact f21 = dao.add(new RelationFact(subj2, rel, obj1));

        // ACT - Retrieve list
        List<RelationFact> list = dao.listByObjectEntity(obj0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(3, list.size());

        // TEST - Curriculums returned in correct order
        Assert.assertTrue(list.contains(f00));
        Assert.assertTrue(list.contains(f10));
        Assert.assertTrue(list.contains(f20));
        Assert.assertFalse(list.contains(f01));
        Assert.assertFalse(list.contains(f11));
        Assert.assertFalse(list.contains(f21));
    }


    @Test
    @Transactional
    public void testListByRelation()
    {
        // PREP - Prepare test objects
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "SUBJ 0"));
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1001, "SUBJ 1"));
        EntityKnowledge obj0 = dao_ek.add(new EntityKnowledge(2000, "OBJ 0"));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(2001, "OBJ 1"));
        RelationKnowledge rel0 = dao_rk.add(new RelationKnowledge("RK 0", "Namespace"));
        RelationKnowledge rel1 = dao_rk.add(new RelationKnowledge("RK 1", "Namespace"));
        RelationFact f000 = dao.add(new RelationFact(subj0, rel0, obj0));
        RelationFact f001 = dao.add(new RelationFact(subj0, rel0, obj1));
        RelationFact f100 = dao.add(new RelationFact(subj1, rel0, obj0));
        RelationFact f101 = dao.add(new RelationFact(subj1, rel0, obj1));
        RelationFact f010 = dao.add(new RelationFact(subj0, rel1, obj0));
        RelationFact f011 = dao.add(new RelationFact(subj0, rel1, obj1));
        RelationFact f110 = dao.add(new RelationFact(subj1, rel1, obj0));
        RelationFact f111 = dao.add(new RelationFact(subj1, rel1, obj1));

        // ACT - Retrieve list
        List<RelationFact> list = dao.listByRelation(rel0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(4, list.size());

        // TEST - Curriculums returned in correct order
        Assert.assertTrue(list.contains(f000));
        Assert.assertTrue(list.contains(f001));
        Assert.assertTrue(list.contains(f100));
        Assert.assertTrue(list.contains(f101));
        Assert.assertFalse(list.contains(f010));
        Assert.assertFalse(list.contains(f011));
        Assert.assertFalse(list.contains(f110));
        Assert.assertFalse(list.contains(f111));
    }


    @Test
    @Transactional
    public void testListBySubjectEntity()
    {
        // ACT - Prepare test objects
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "SUBJ 0"));
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1001, "SUBJ 1"));
        EntityKnowledge obj0 = dao_ek.add(new EntityKnowledge(2000, "OBJ 0"));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(2001, "OBJ 1"));
        EntityKnowledge obj2 = dao_ek.add(new EntityKnowledge(2001, "OBJ 2"));
        RelationKnowledge rel = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact f00 = dao.add(new RelationFact(subj0, rel, obj0));
        RelationFact f01 = dao.add(new RelationFact(subj0, rel, obj1));
        RelationFact f02 = dao.add(new RelationFact(subj0, rel, obj2));
        RelationFact f10 = dao.add(new RelationFact(subj1, rel, obj0));
        RelationFact f11 = dao.add(new RelationFact(subj1, rel, obj1));
        RelationFact f12 = dao.add(new RelationFact(subj1, rel, obj2));

        // ACT - Retrieve list
        List<RelationFact> list = dao.listBySubjectEntity(subj0.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(3, list.size());

        // TEST - Curriculums returned in correct order
        Assert.assertTrue(list.contains(f00));
        Assert.assertTrue(list.contains(f01));
        Assert.assertTrue(list.contains(f02));
        Assert.assertFalse(list.contains(f10));
        Assert.assertFalse(list.contains(f11));
        Assert.assertFalse(list.contains(f12));
    }


    @Test
    @Transactional
    public void testUpdate()
    {
        // ACT - Prepare test objects
        EntityKnowledge subj1 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge obj1 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        RelationKnowledge rel1 = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        EntityKnowledge subj2 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));
        EntityKnowledge obj2 = dao_ek.add(new EntityKnowledge(1001, "EK 1001"));
        RelationKnowledge rel2 = dao_rk.add(new RelationKnowledge("Name", "Namespace"));
        RelationFact fact = dao.add(new RelationFact(subj1, rel1, obj1));
        fact.setObject(obj2);
        fact.setRelation(rel2);
        fact.setSubject(subj2);
        dao.update(fact);
        RelationFact fact_copy = dao.get(fact.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(fact_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(fact, fact_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(fact.getNodeId(), fact_copy.getNodeId());
        Assert.assertEquals(fact.getSubject(), fact_copy.getSubject());
        Assert.assertEquals(fact.getObject(), fact_copy.getObject());
        Assert.assertEquals(fact.getRelation(), fact_copy.getRelation());
    }
}
