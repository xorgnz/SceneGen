package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.core.persist.neo4j.WrongNodeTypeException;
import org.memehazard.wheel.tutoring.dao.CurriculumItemDAO;
import org.memehazard.wheel.tutoring.dao.EntityKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.RelationKnowledgeDAO;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
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
public class RelationKnowledgeDAOTest
{
    // Components
    @Autowired
    private RelationKnowledgeDAO dao;
    @Autowired
    private CurriculumItemDAO    dao_ci;
    @Autowired
    private EntityKnowledgeDAO   dao_ek;
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
        // ACT - Add and retrieve curriculum
        RelationKnowledge rcsi = dao.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE));
        RelationKnowledge rcsi_copy = dao.get(rcsi.getNodeId());

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(1, dao.count());

        // TEST - Retrieved object
        Assert.assertNotNull(rcsi_copy);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertNotSame(rcsi, rcsi_copy);

        // TEST - does retrieved curriculum share all parameter values?
        Assert.assertEquals(rcsi.getNodeId(), rcsi_copy.getNodeId());
        Assert.assertEquals(rcsi.getName(), rcsi_copy.getName());
        Assert.assertEquals(rcsi.getNamespace(), rcsi_copy.getNamespace());
    }


    @Test
    @Transactional
    public void testDelete()
    {
        // ACT - Prepare test objects
        RelationKnowledge ci = dao.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE));
        dao.add(new RelationKnowledge(TestData.RK_NAME[1], TestData.RK_NAMESPACE));
        dao.add(new RelationKnowledge(TestData.RK_NAME[2], TestData.RK_NAMESPACE));

        // TEST - Correct count of curricula in DB
        Assert.assertEquals(3, dao.count());

        // ACT - Delete
        dao.delete(ci);

        // TEST - is retrieved curriculum same object as that added? Should fail
        Assert.assertEquals(2, dao.count());
    }


    @Test
    @Transactional
    public void testFind()
    {
        // ACT - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        dao.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci));

        // TEST - Match found for same values as initial RK
        Assert.assertNotNull("Match found", dao.find(ci.getNodeId(), TestData.RK_NAME[0], TestData.RK_NAMESPACE));

        // TEST - No match found with different name, namespace, or CI
        Assert.assertNull("No match - different name", dao.find(ci.getNodeId(), TestData.RK_NAME[1], TestData.RK_NAMESPACE));
        Assert.assertNull("No match - different namespace", dao.find(ci.getNodeId(), TestData.RK_NAME[0], TestData.RK_NAMESPACE + "alt"));
        Assert.assertNull("No match - different CI Id", dao.find(ci.getNodeId() + 1000, TestData.RK_NAME[0], TestData.RK_NAMESPACE));
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
        EntityKnowledge subj0 = dao_ek.add(new EntityKnowledge(1000, "EK 1000"));

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


    @Test
    @Transactional
    public void testListAllByCurriculum()
    {
        // ACT - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        dao.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci));
        dao.add(new RelationKnowledge(TestData.RK_NAME[2], TestData.RK_NAMESPACE, ci)); // Insert out of order
        dao.add(new RelationKnowledge(TestData.RK_NAME[1], TestData.RK_NAMESPACE, ci));
        dao_ek.add(new EntityKnowledge(1001, "FMA Label", ci));

        // Retrieve list
        List<RelationKnowledge> list = dao.listByCurriculumItem(ci.getNodeId());

        // TEST - List is not null
        Assert.assertNotNull(list);

        // TEST - List contains correct number of objects
        Assert.assertEquals(3, list.size());

        // TEST - Curriculums returned in correct order
        Assert.assertEquals(TestData.RK_NAME[0], list.get(0).getName());
        Assert.assertEquals(TestData.RK_NAME[1], list.get(1).getName());
        Assert.assertEquals(TestData.RK_NAME[2], list.get(2).getName());
    }


    @Test
    @Transactional
    public void testListAllByCurriculum_NoItems()
    {
        log.trace("Start testListAll_EmptyDB");

        // ACT - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));

        // ACT - Retrieve list
        List<RelationKnowledge> list = dao.listByCurriculumItem(ci.getNodeId());

        // TEST - List is empty
        Assert.assertEquals(0, list.size());

        log.trace("Finish testListAll_EmptyDB");
    }


    @Test
    @Transactional
    public void testUpdate()
    {
        // ACT - Prepare test objects
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        CurriculumItem ci2 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1]));
        RelationKnowledge rcsi = dao.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci1));
        rcsi.setName(TestData.RK_NAME[0] + 1000);
        rcsi.setNamespace(TestData.RK_NAMESPACE + " alt");
        rcsi.setCurriculumItem(ci2);
        dao.update(rcsi);
        RelationKnowledge rcsi_copy = dao.get(rcsi.getNodeId());

        // TEST - Retrieved object is not null
        Assert.assertNotNull(rcsi_copy);

        // TEST - Retrieved object is not same as original
        Assert.assertNotSame(rcsi, rcsi_copy);

        // TEST - Retrieved object shares all parameter values
        Assert.assertEquals(rcsi.getNodeId(), rcsi_copy.getNodeId());
        Assert.assertEquals(rcsi.getCurriculumItem(), rcsi_copy.getCurriculumItem());
        Assert.assertEquals(rcsi.getName(), rcsi_copy.getName());
        Assert.assertEquals(rcsi.getNamespace(), rcsi_copy.getNamespace());
    }
}
