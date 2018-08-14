package org.memehazard.wheel.tutoring.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.query.facade.AbstractMockQueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.dao.BayesDAO;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.dao.CurriculumItemDAO;
import org.memehazard.wheel.tutoring.dao.EnrolmentDAO;
import org.memehazard.wheel.tutoring.dao.EntityKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.LabelFactDAO;
import org.memehazard.wheel.tutoring.dao.LabelKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.RelationFactDAO;
import org.memehazard.wheel.tutoring.dao.RelationKnowledgeDAO;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.Enrolment;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.LabelFact;
import org.memehazard.wheel.tutoring.model.LabelKnowledge;
import org.memehazard.wheel.tutoring.model.RelationFact;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.memehazard.wheel.tutoring.test.TestData;
import org.memehazard.wheel.tutoring.test.TutoringTestMyBatisDAO;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


/**
 * Tests all complex methods in the <code>TutoringFacade</code>
 * 
 * @author xorgnz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class TutoringFacadeTest
{
    @Autowired
    private BayesDAO               dao_bayes;
    @Autowired
    private CurriculumDAO          dao_c;
    @Autowired
    private CurriculumItemDAO      dao_ci;
    @Autowired
    private EntityKnowledgeDAO     dao_ek;
    @Autowired
    private EnrolmentDAO           dao_enrolment;
    @Autowired
    private LabelFactDAO           dao_lFact;
    @Autowired
    private LabelKnowledgeDAO      dao_lk;
    @Autowired
    private RelationFactDAO        dao_rFact;

    @Autowired
    private RelationKnowledgeDAO   dao_rk;
    @Autowired
    private TutoringTestMyBatisDAO dao_test;
    @Autowired
    private UserDAO                dao_user;
    @Autowired
    private TutoringFacade         facade;
    private Logger                 log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private Neo4jUtilities         utils;


    /**
     * Before tests, clear the database, prepare standard test objects
     */
    @BeforeTransaction
    public void beforeTransaction()
    {
        log.trace("------------------- BEFORE");

        // ACT - Clear DB
        dao_test.deleteAll();
        utils.clearDatabase();
    }


    /**
     * Tests addCurriculum
     */
    @Test
    @Transactional
    public void test_addCurriculum()
    {
        Curriculum c = new Curriculum("name1", "creator2", "desc3");
        facade.addCurriculum(c);

        Curriculum c_copy = facade.getCurriculum(c.getNodeId());
        Assert.assertEquals(c.getNodeId(), c_copy.getNodeId());
        Assert.assertEquals(c.getName(), c_copy.getName());
        Assert.assertEquals(c.getCreatorName(), c_copy.getCreatorName());
        Assert.assertEquals(c.getDescription(), c_copy.getDescription());
    }


    /**
     * Tests addCurriculumItem
     */
    @Test
    @Transactional
    public void test_addCurriculumItem()
    {
        Curriculum c = new Curriculum("name1", "creator2", "desc3");
        facade.addCurriculum(c);

        CurriculumItem ci = new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]);
        facade.addCurriculumItem(ci);

        CurriculumItem ci_copy = facade.getCurriculumItem(ci.getNodeId());
        Assert.assertEquals(ci.getNodeId(), ci_copy.getNodeId());
        Assert.assertEquals(ci.getName(), ci_copy.getName());
        Assert.assertEquals(ci.getDescription(), ci_copy.getDescription());
    }


    /**
     * Tests adding an EK node when that EK node already exists as a hanging node.
     * 
     * Key tests:
     * - 1A - Pre-existing EK is returned on method invocation
     * - 1B - Pre-existing EK is now linked
     * - 2 - Existing hanging node is associated as object
     * - 3 - Existing hanging node is associated as subject
     * - 4 - Existing linked nodes is associated as object
     * - 5 - Existing linked nodes is associated as subject
     * - 6 - New hanging object node is created
     * - 7 - New hanging subject node is created
     * - 8 - No extraneous nodes are created
     * - 9 - Relation Facts are created
     * - 10 - Label Facts are created (if necessary)
     * 
     * Initial model:
     * - C, CI
     * - RK: 0
     * - EK: 0 - hanging
     * - EK: 1 - hanging
     * - EK: 2 - hanging
     * - EK: 3 - linked
     * - EK: 4 - linked
     * - EK: 7 - linked, anchor for hanging EK nodes
     * - Fact: 7 --> 0
     * - Fact: 7 --> 1
     * - Fact: 7 --> 2
     * 
     * Network:
     * - 0 -0-> 1
     * - 0 -0-> 2
     * - 0 -0-> 3
     * - 0 -0-> 4
     * - 0 -0-> 5
     * - 0 -0-> 6
     * - 7 -0-> 0
     * - 7 -0-> 1
     * - 7 -0-> 2
     * 
     * Expected model:
     * - C, CI
     * - RK: 0
     * - EK: 0 - now linked
     * - EK: 1 - associated as hanging object (2)
     * - EK: 2 - associated as hanging subject (3)
     * - EK: 3 - associated as linked object (4)
     * - EK: 4 - associated as linked subject (5)
     * - EK: 5 - created and associated as hanging object (6)
     * - EK: 6 - created and associated as hanging subject (7)
     * - Fact: 0 --> 1 (2, 9)
     * - Fact: 0 --> 2 (3, 9)
     * - Fact: 0 --> 3 (4, 9)
     * - Fact: 0 --> 4 (5, 9)
     * - Fact: 0 --> 5 (6, 9)
     * - Fact: 0 --> 6 (7, 9)
     * - Fact: 7 --> 0
     * - Fact: 7 --> 1
     * - Fact: 7 --> 2
     * 
     * @throws IOException
     * @throws ParserException
     */
    @Test
    @Transactional
    public void test_addEntityKnowledge_alreadyExistsHanging()
            throws ParserException, IOException
    {
        log.trace(".test_addEntityKnowledge_alreadyExistsHanging");

        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for the following FMA network
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                List<Relationship> relationships = new ArrayList<Relationship>();

                Entity e0 = new Entity(TestData.EK_FMA_LABEL[0], TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);
                Entity e1 = new Entity(TestData.EK_FMA_LABEL[1], TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1]);
                Entity e2 = new Entity(TestData.EK_FMA_LABEL[2], TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2]);
                Entity e3 = new Entity(TestData.EK_FMA_LABEL[3], TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3]);
                Entity e4 = new Entity(TestData.EK_FMA_LABEL[4], TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4]);
                Entity e5 = new Entity(TestData.EK_FMA_LABEL[5], TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5]);
                Entity e6 = new Entity(TestData.EK_FMA_LABEL[6], TestData.EK_FMA_ID[6], TestData.EK_FMA_LABEL[6]);
                Entity e7 = new Entity(TestData.EK_FMA_LABEL[7], TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7]);
                Relation rel = new Relation(TestData.RK_STRING[0]);

                if (fmaid == TestData.EK_FMA_ID[0])
                {
                    relationships.add(new Relationship(e0, rel, e1));
                    relationships.add(new Relationship(e0, rel, e2));
                    relationships.add(new Relationship(e0, rel, e3));
                    relationships.add(new Relationship(e0, rel, e4));
                    relationships.add(new Relationship(e0, rel, e5));
                    relationships.add(new Relationship(e0, rel, e6));
                    relationships.add(new Relationship(e7, rel, e0));
                }
                else if (fmaid == TestData.EK_FMA_ID[1])
                {

                    relationships.add(new Relationship(e0, rel, e1));
                    relationships.add(new Relationship(e7, rel, e1));
                }
                else if (fmaid == TestData.EK_FMA_ID[2])
                {
                    relationships.add(new Relationship(e0, rel, e2));
                    relationships.add(new Relationship(e7, rel, e2));
                }
                else if (fmaid == TestData.EK_FMA_ID[3])
                {
                    relationships.add(new Relationship(e0, rel, e3));
                }
                else if (fmaid == TestData.EK_FMA_ID[4])
                {
                    relationships.add(new Relationship(e0, rel, e4));
                }
                else if (fmaid == TestData.EK_FMA_ID[5])
                {
                    relationships.add(new Relationship(e0, rel, e5));
                }
                else if (fmaid == TestData.EK_FMA_ID[6])
                {
                    relationships.add(new Relationship(e0, rel, e6));
                }
                else if (fmaid == TestData.EK_FMA_ID[7])
                {
                    relationships.add(new Relationship(e7, rel, e0));
                    relationships.add(new Relationship(e7, rel, e1));
                    relationships.add(new Relationship(e7, rel, e2));
                }

                return relationships;
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], null));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        EntityKnowledge ek4 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], ci));
        EntityKnowledge ek7 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7], ci));
        dao_rFact.add(new RelationFact(ek7, rk0, ek0));
        dao_rFact.add(new RelationFact(ek7, rk0, ek1));
        dao_rFact.add(new RelationFact(ek7, rk0, ek2));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add entity
        EntityKnowledge ek0_copy = facade.addEntityKnowledge(ci.getNodeId(), TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);

        // PREP - Get affected objects for test
        ek1 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[1]);
        ek2 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[2]);
        ek3 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[3]);
        ek4 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[4]);
        EntityKnowledge ek5 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[5]);
        EntityKnowledge ek6 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[6]);
        RelationFact f01 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek1.getNodeId());
        RelationFact f02 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek2.getNodeId());
        RelationFact f03 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek3.getNodeId());
        RelationFact f04 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek4.getNodeId());
        RelationFact f05 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek5.getNodeId());
        RelationFact f06 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek6.getNodeId());

        // TEST - Were objects created?
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(8, dao_ek.count());
        Assert.assertEquals(1, dao_rk.count());
        Assert.assertEquals(9, dao_rFact.count());

        // TEST - Are correct EK nodes present?
        Assert.assertNotNull("Newly linked node not linked", ek0_copy.getCurriculumItem());
        Assert.assertNotNull("Method did not return newly linked node", ek0_copy);
        Assert.assertNotNull("Method did not return newly linked node ID", ek0_copy.getNodeId());
        Assert.assertNotNull("Missing entity 1 (pre-existing hanging object)", ek1);
        Assert.assertNotNull("Missing entity 2 (pre-existing hanging subject)", ek2);
        Assert.assertNotNull("Missing entity 3 (linked object)", ek3);
        Assert.assertNotNull("Missing entity 4 (linked subject)", ek4);
        Assert.assertNotNull("Missing entity 5 (new hanging object)", ek5);
        Assert.assertNotNull("Missing entity 6 (new hanging subject)", ek6);

        // TEST - Are correct facts present?
        Assert.assertNotNull("Missing E0 -> E1", f01);
        Assert.assertNotNull("Missing E0 -> E2", f02);
        Assert.assertNotNull("Missing E0 -> E3", f03);
        Assert.assertNotNull("Missing E0 -> E4", f04);
        Assert.assertNotNull("Missing E0 -> E5", f05);
        Assert.assertNotNull("Missing E0 -> E6", f06);

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(7, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added EK", dao_bayes.get(ek0.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f01.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f02.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f03.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f04.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f05.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f06.getNodeId(), u.getId()));
    }


    /**
     * Tests adding an EK node when that EK node already exists as a linked node.
     * 
     * Key tests:
     * - Node counts do not change
     * - Pre-existing node is returned
     * - Pre-existing node is not changed.
     * - New relationships are not instantiated as facts
     * - No exception is thrown.
     * 
     * Initial model:
     * - C, CI
     * - RK: 0 - necessary in case fact creation is attempted
     * - EK: 0 - linked
     * 
     * Network:
     * - 0 -0-> 1
     * 
     * Expected model:
     * - C, CI
     * - RK: 0
     * - EK: 0
     * - No fact or EK nodes are created
     * 
     * @throws IOException
     * @throws ParserException
     */
    @Test
    @Transactional
    public void test_addEntityKnowledge_alreadyExistsLinked()
            throws ParserException, IOException
    {
        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for FMA network (see outer method)
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                List<Relationship> relationships = new ArrayList<Relationship>();

                Entity e0 = new Entity(TestData.EK_FMA_LABEL[0], TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);
                Entity e1 = new Entity(TestData.EK_FMA_LABEL[1], TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1]);
                Relation rel = new Relation(TestData.RK_STRING[0]);

                switch (fmaid)
                {
                case 0:
                    relationships.add(new Relationship(e0, rel, e1));
                    break;
                }

                return relationships;
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));

        // ACT - Add entity
        EntityKnowledge ek0_copy = facade.addEntityKnowledge(ci.getNodeId(), TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);

        // TEST - Were objects created?
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(1, dao_ek.count());
        Assert.assertEquals(1, dao_rk.count());
        Assert.assertEquals(0, dao_rFact.count());

        // TEST - Are correct EK nodes present?
        Assert.assertNotNull("Did not return pre-existing node", ek0_copy);
        Assert.assertEquals("Did not return pre-existing node", ek0_copy, ek0);
    }


    /**
     * Tests adding an EK node to the domain model
     * 
     * Key tests:
     * - 1A - Newly created EK is returned on method invocation
     * - 1B - Newly created EK can be found
     * - 2 - Existing hanging node is associated as object
     * - 3 - Existing hanging node is associated as subject
     * - 4 - Existing linked nodes is associated as object
     * - 5 - Existing linked nodes is associated as subject
     * - 6 - New hanging object node is created
     * - 7 - New hanging subject node is created
     * - 8 - No extraneous nodes are created
     * - 9 - Facts are created
     * 
     * Initial model:
     * - C, CI
     * - RK: 0
     * - EK: 1 - hanging
     * - EK: 2 - hanging
     * - EK: 3 - linked
     * - EK: 4 - linked
     * - EK: 7 - linked, anchor for hanging EK nodes
     * - Fact: 7 --> 1
     * - Fact: 7 --> 2
     * 
     * Network:
     * - 0 -0-> 1
     * - 0 -0-> 2
     * - 0 -0-> 3
     * - 0 -0-> 4
     * - 0 -0-> 5
     * - 0 -0-> 6
     * - 7 -0-> 1
     * - 7 -0-> 2
     * 
     * Expected model:
     * - C, CI
     * - RK: 0
     * - EK: 0 - created
     * - EK: 1 - associated as hanging object (2)
     * - EK: 2 - associated as hanging subject (3)
     * - EK: 3 - associated as linked object (4)
     * - EK: 4 - associated as linked subject (5)
     * - EK: 5 - created and associated as hanging object (6)
     * - EK: 6 - created and associated as hanging subject (7)
     * - Fact: 0 --> 1 (2, 9)
     * - Fact: 0 --> 2 (3, 9)
     * - Fact: 0 --> 3 (4, 9)
     * - Fact: 0 --> 4 (5, 9)
     * - Fact: 0 --> 5 (6, 9)
     * - Fact: 0 --> 6 (7, 9)
     * - Fact: 7 --> 1
     * - Fact: 7 --> 2
     * 
     * @throws IOException
     * @throws ParserException
     */
    @Test
    @Transactional
    public void test_addEntityKnowledge_FreshNode()
            throws ParserException, IOException
    {
        log.trace(".test_addEntityKnowledge_FreshNode");

        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for the following FMA network
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                List<Relationship> relationships = new ArrayList<Relationship>();

                Entity e0 = new Entity(TestData.EK_FMA_LABEL[0], TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);
                Entity e1 = new Entity(TestData.EK_FMA_LABEL[1], TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1]);
                Entity e2 = new Entity(TestData.EK_FMA_LABEL[2], TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2]);
                Entity e3 = new Entity(TestData.EK_FMA_LABEL[3], TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3]);
                Entity e4 = new Entity(TestData.EK_FMA_LABEL[4], TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4]);
                Entity e5 = new Entity(TestData.EK_FMA_LABEL[5], TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5]);
                Entity e6 = new Entity(TestData.EK_FMA_LABEL[6], TestData.EK_FMA_ID[6], TestData.EK_FMA_LABEL[6]);
                Entity e7 = new Entity(TestData.EK_FMA_LABEL[7], TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7]);
                Relation rel = new Relation(TestData.RK_STRING[0]);

                if (fmaid == TestData.EK_FMA_ID[0])
                {
                    relationships.add(new Relationship(e0, rel, e1));
                    relationships.add(new Relationship(e0, rel, e2));
                    relationships.add(new Relationship(e0, rel, e3));
                    relationships.add(new Relationship(e0, rel, e4));
                    relationships.add(new Relationship(e0, rel, e5));
                    relationships.add(new Relationship(e0, rel, e6));
                }
                else if (fmaid == TestData.EK_FMA_ID[1])
                {
                    relationships.add(new Relationship(e0, rel, e1));
                    relationships.add(new Relationship(e7, rel, e1));
                }
                else if (fmaid == TestData.EK_FMA_ID[2])
                {
                    relationships.add(new Relationship(e0, rel, e2));
                    relationships.add(new Relationship(e7, rel, e2));
                }
                else if (fmaid == TestData.EK_FMA_ID[3])
                {
                    relationships.add(new Relationship(e0, rel, e3));
                }
                else if (fmaid == TestData.EK_FMA_ID[4])
                {
                    relationships.add(new Relationship(e0, rel, e4));
                }
                else if (fmaid == TestData.EK_FMA_ID[5])
                {
                    relationships.add(new Relationship(e0, rel, e5));
                }
                else if (fmaid == TestData.EK_FMA_ID[6])
                {
                    relationships.add(new Relationship(e0, rel, e6));
                }
                else if (fmaid == TestData.EK_FMA_ID[7])
                {
                    relationships.add(new Relationship(e7, rel, e1));
                    relationships.add(new Relationship(e7, rel, e2));
                }

                return relationships;
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        EntityKnowledge ek4 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], ci));
        EntityKnowledge ek7 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7], ci));
        dao_rFact.add(new RelationFact(ek7, rk0, ek1));
        dao_rFact.add(new RelationFact(ek7, rk0, ek2));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add entity
        EntityKnowledge ek0 = facade.addEntityKnowledge(ci.getNodeId(), TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);

        // PREP - Get affected objects for test
        EntityKnowledge ek0_copy = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[0]);
        ek1 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[1]);
        ek2 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[2]);
        ek3 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[3]);
        ek4 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[4]);
        EntityKnowledge ek5 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[5]);
        EntityKnowledge ek6 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[6]);
        RelationFact f01 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek1.getNodeId());
        RelationFact f02 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek2.getNodeId());
        RelationFact f03 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek3.getNodeId());
        RelationFact f04 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek4.getNodeId());
        RelationFact f05 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek5.getNodeId());
        RelationFact f06 = dao_rFact.find(ek0.getNodeId(), rk0.getNodeId(), ek6.getNodeId());

        // TEST - Were objects created?
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(8, dao_ek.count());
        Assert.assertEquals(1, dao_rk.count());
        Assert.assertEquals(8, dao_rFact.count());

        // TEST - Are correct EK nodes present?
        Assert.assertNotNull("Method did not return newly added node", ek0);
        Assert.assertNotNull("Method did not return newly added node ID", ek0.getNodeId());
        Assert.assertNotNull("Newly added node not found in model", ek0_copy);
        Assert.assertNotNull("Missing entity 1 (pre-existing hanging object)", ek1);
        Assert.assertNotNull("Missing entity 2 (pre-existing hanging subject)", ek2);
        Assert.assertNotNull("Missing entity 3 (linked object)", ek3);
        Assert.assertNotNull("Missing entity 4 (linked subject)", ek4);
        Assert.assertNotNull("Missing entity 5 (new hanging object)", ek5);
        Assert.assertNotNull("Missing entity 6 (new hanging subject)", ek6);

        // TEST - Are correct facts present?
        Assert.assertNotNull("Missing E0 -> E1", f01);
        Assert.assertNotNull("Missing E0 -> E2", f02);
        Assert.assertNotNull("Missing E0 -> E3", f03);
        Assert.assertNotNull("Missing E0 -> E4", f04);
        Assert.assertNotNull("Missing E0 -> E5", f05);
        Assert.assertNotNull("Missing E0 -> E6", f06);

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(7, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added EK", dao_bayes.get(ek0.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f01.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f02.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f03.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f04.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f05.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f06.getNodeId(), u.getId()));
    }


    /**
     * Tests adding an EK node to the domain model.
     * 
     * Checks
     * - Label Fact is created on EK addition when LK exists
     */
    @Test
    @Transactional
    public void test_addEntityKnowledge_withLabelKnowledge() throws IOException, ParserException
    {
        log.trace(".test_addEntityKnowledge_withLabelKnowledge");

        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for the following FMA network
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                return new ArrayList<Relationship>();
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add entity
        EntityKnowledge ek = facade.addEntityKnowledge(ci.getNodeId(), TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);

        // PREP - Get affected objects for test
        EntityKnowledge ek_copy = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[0]);
        LabelFact lf = dao_lFact.findByEntityKnowledge(ek.getNodeId());

        // TEST - Were objects created?
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(1, dao_ek.count());
        Assert.assertEquals(1, dao_lFact.count());

        // TEST - Is correct EK node present?
        Assert.assertNotNull("Method did not return newly added node", ek);
        Assert.assertNotNull("Method did not return newly added node ID", ek.getNodeId());
        Assert.assertNotNull("Newly added node not found in model", ek_copy);

        // TEST - Is correct label fact present?
        Assert.assertNotNull("Missing Label Fact", lf);

        // TEST - Is label fact correctly linked
        Assert.assertNotNull("Missing EK on LF", lf.getEntityKnowledge());
        Assert.assertNotNull("Missing LK on LF", lf.getLabelKnowledge());
        Assert.assertEquals("Not linked to EK", ek.getNodeId(), lf.getEntityKnowledge().getNodeId());
        Assert.assertEquals("Not linked to LK", lk.getNodeId(), lf.getLabelKnowledge().getNodeId());

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(2, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added EK", dao_bayes.get(ek.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(lf.getNodeId(), u.getId()));
    }


    /**
     * Tests adding an EK node to the domain model.
     * 
     * Checks
     * - Label Fact is not created on EK addition when LK does not exist
     */
    @Test
    @Transactional
    public void test_addEntityKnowledge_withoutLabelKnowledge() throws IOException, ParserException
    {
        log.trace(".test_addEntityKnowledge_withLabelKnowledge");

        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for the following FMA network
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                return new ArrayList<Relationship>();
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add entity
        EntityKnowledge ek = facade.addEntityKnowledge(ci.getNodeId(), TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);

        // PREP - Get affected objects for test
        EntityKnowledge ek_copy = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[0]);
        LabelFact lf = dao_lFact.findByEntityKnowledge(ek.getNodeId());

        // TEST - Were objects created?
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(1, dao_ek.count());
        Assert.assertEquals(0, dao_lFact.count());

        // TEST - Is correct EK node present?
        Assert.assertNotNull("Method did not return newly added node", ek);
        Assert.assertNotNull("Method did not return newly added node ID", ek.getNodeId());
        Assert.assertNotNull("Newly added node not found in model", ek_copy);

        // TEST - Is correct label fact present?
        Assert.assertNull("Label Fact incorrectly created", lf);

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(1, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added EK", dao_bayes.get(ek.getNodeId(), u.getId()));
    }


    /**
     * Test adding an LK node to the domain model.
     * 
     * Checks:
     * - That an LK is added
     * - That LFs are created for any EKs linked to LK's CI
     * - That LFs are not created for any EKs not linked to LK's CI
     * - That appropriate BayesValues are created for enrolled students
     */
    @Test
    @Transactional
    public void test_addLabelKnowledge()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add LK Node
        LabelKnowledge lk = facade.addLabelKnowledge(ci.getNodeId());

        // TEST - Correct objects counts
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(3, dao_ek.count());
        Assert.assertEquals(1, dao_lk.count());
        Assert.assertEquals(2, dao_lFact.count());

        // PREP - Get affected objects
        LabelKnowledge lk_copy = dao_lk.findByCurriculumItem(ci.getNodeId());
        LabelFact lf0 = dao_lFact.findByEntityKnowledge(ek0.getNodeId());
        LabelFact lf1 = dao_lFact.findByEntityKnowledge(ek1.getNodeId());
        LabelFact lf2 = dao_lFact.findByEntityKnowledge(ek2.getNodeId());

        // TEST - Is correct LK present?
        Assert.assertNotNull("Method did not return newly added node", lk);
        Assert.assertNotNull("Method did not return newly added node ID", lk.getNodeId());
        Assert.assertNotNull("Newly added node not found in model", lk_copy);

        // TEST - Are correct facts present?
        Assert.assertNotNull("Missing LF for linked EK 0", lf0);
        Assert.assertNull("Anomalous LF for hanging EK 1", lf1);
        Assert.assertNotNull("Missing LF for linked EK 2", lf2);

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(3, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added LK", dao_bayes.get(lk.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(lf0.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(lf2.getNodeId(), u.getId()));
    }


    /**
     * Test adding an LK node to the domain model when such a node already exists
     * 
     * Checks:
     * - That no additional LK is added
     * - That no LabelFacts are created
     */
    @Test
    @Transactional
    public void test_addLabelKnowledge_alreadyExists()
    {
        // PREP - Create test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci));
        dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));

        // ACT - Add LK Node
        LabelKnowledge lk0_copy = facade.addLabelKnowledge(ci.getNodeId());

        // TEST - Correct object counts
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(1, dao_ek.count());
        Assert.assertEquals(1, dao_lk.count());
        Assert.assertEquals(0, dao_lFact.count());

        // TEST - Are correct RK nodes present?
        Assert.assertNotNull("Did not return pre-existing node", lk0_copy);
        Assert.assertEquals("Did not return pre-existing node", lk0_copy, lk0);
    }


    /**
     * Tests adding a RK node to the domain model
     * 
     * Key tests:
     * - 1A - Newly created RK is returned on method invocation
     * - 1B - Newly created RK can be found
     * - 2 - Existing hanging node is associated as object
     * - 3 - Existing hanging node is associated as subject
     * - 4 - Existing linked nodes is associated as object
     * - 5 - Existing linked nodes is associated as subject
     * - 6 - New hanging object node is created
     * - 7 - New hanging subject node is created
     * - 8 - No extraneous nodes are created
     * - 9 - Facts are created
     * 
     * Initial model:
     * - C, CI
     * - RK: 0
     * - EK: 0 - linked
     * - EK: 1 - hanging
     * - EK: 2 - hanging
     * - EK: 3 - linked
     * - EK: 4 - linked
     * - EK: 7 - linked, anchor for hanging EK nodes
     * - Fact: 7 -0-> 1
     * - Fact: 7 -0-> 2
     * 
     * Network:
     * - 0 -1-> 1
     * - 0 -1-> 2
     * - 0 -1-> 3
     * - 0 -1-> 4
     * - 0 -1-> 5
     * - 0 -1-> 6
     * - 7 -0-> 1
     * - 7 -0-> 2
     * 
     * Expected model:
     * - C, CI
     * - RK: 0
     * - RK: 1 - created
     * - EK: 0 - linked
     * - EK: 1 - associated as hanging object (2)
     * - EK: 2 - associated as hanging subject (3)
     * - EK: 3 - associated as linked object (4)
     * - EK: 4 - associated as linked subject (5)
     * - EK: 5 - created and associated as hanging object (6)
     * - EK: 6 - created and associated as hanging subject (7)
     * - EK: 7 - anchor
     * - Fact: 0 -1-> 1 (2, 9)
     * - Fact: 0 -1-> 2 (3, 9)
     * - Fact: 0 -1-> 3 (4, 9)
     * - Fact: 0 -1-> 4 (5, 9)
     * - Fact: 0 -1-> 5 (6, 9)
     * - Fact: 0 -1-> 6 (7, 9)
     * - Fact: 7 -0-> 1
     * - Fact: 7 -0-> 2
     * 
     * @throws IOException
     * @throws XMLException
     */
    @Test
    @Transactional
    public void test_addRelationKnowledge()
            throws ParserException, IOException
    {
        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for the following FMA network
            // - EK0 -RK1-> EK 1
            // - EK0 -RK1-> EK 2
            // - EK0 -RK1-> EK 3
            // - EK0 -RK1-> EK 4
            // - EK0 -RK1-> EK 5
            // - EK0 -RK1-> EK 6
            // - EK7 -RK0-> EK 1
            // - EK7 -RK0-> EK 2
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                List<Relationship> relationships = new ArrayList<Relationship>();

                Entity e0 = new Entity(TestData.EK_FMA_LABEL[0], TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0]);
                Entity e1 = new Entity(TestData.EK_FMA_LABEL[1], TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1]);
                Entity e2 = new Entity(TestData.EK_FMA_LABEL[2], TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2]);
                Entity e3 = new Entity(TestData.EK_FMA_LABEL[3], TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3]);
                Entity e4 = new Entity(TestData.EK_FMA_LABEL[4], TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4]);
                Entity e5 = new Entity(TestData.EK_FMA_LABEL[5], TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5]);
                Entity e6 = new Entity(TestData.EK_FMA_LABEL[6], TestData.EK_FMA_ID[6], TestData.EK_FMA_LABEL[6]);
                Entity e7 = new Entity(TestData.EK_FMA_LABEL[7], TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7]);
                Relation rk0 = new Relation(TestData.RK_STRING[0]);
                Relation rk1 = new Relation(TestData.RK_STRING[1]);

                if (fmaid == TestData.EK_FMA_ID[0])
                {
                    relationships.add(new Relationship(e0, rk1, e1));
                    relationships.add(new Relationship(e0, rk1, e2));
                    relationships.add(new Relationship(e0, rk1, e3));
                    relationships.add(new Relationship(e0, rk1, e4));
                    relationships.add(new Relationship(e0, rk1, e5));
                    relationships.add(new Relationship(e0, rk1, e6));
                }
                else if (fmaid == TestData.EK_FMA_ID[1])
                {
                    relationships.add(new Relationship(e0, rk1, e1));
                    relationships.add(new Relationship(e7, rk0, e1));
                }
                else if (fmaid == TestData.EK_FMA_ID[2])
                {
                    relationships.add(new Relationship(e0, rk1, e2));
                    relationships.add(new Relationship(e7, rk0, e2));
                }
                else if (fmaid == TestData.EK_FMA_ID[3])
                {
                    relationships.add(new Relationship(e0, rk1, e3));
                }
                else if (fmaid == TestData.EK_FMA_ID[4])
                {
                    relationships.add(new Relationship(e0, rk1, e4));
                }
                else if (fmaid == TestData.EK_FMA_ID[5])
                {
                    relationships.add(new Relationship(e0, rk1, e5));
                }
                else if (fmaid == TestData.EK_FMA_ID[6])
                {
                    relationships.add(new Relationship(e0, rk1, e6));
                }
                else if (fmaid == TestData.EK_FMA_ID[7])
                {
                    relationships.add(new Relationship(e7, rk0, e1));
                    relationships.add(new Relationship(e7, rk0, e2));
                }

                return relationships;
            }
        });

        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], null));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        EntityKnowledge ek4 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], ci));
        EntityKnowledge ek7 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[7], TestData.EK_FMA_LABEL[7], ci));
        dao_rFact.add(new RelationFact(ek7, rk0, ek1));
        dao_rFact.add(new RelationFact(ek7, rk0, ek2));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // ACT - Add RK node
        RelationKnowledge rk1 = facade.addRelationKnowledge(ci.getNodeId(), TestData.RK_NAME[1], TestData.RK_NAMESPACE);

        // TEST - Correct objects counts
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(8, dao_ek.count());
        Assert.assertEquals(2, dao_rk.count());
        Assert.assertEquals(8, dao_rFact.count());

        // PREP - Get affected objects
        RelationKnowledge rk1_copy = dao_rk.find(ci.getNodeId(), TestData.RK_NAME[1], TestData.RK_NAMESPACE);
        ek0 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[0]);
        ek1 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[1]);
        ek2 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[2]);
        ek3 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[3]);
        ek4 = dao_ek.findLinkedByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[4]);
        EntityKnowledge ek5 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[5]);
        EntityKnowledge ek6 = dao_ek.findHangingByCurriculumItemAndEntity(ci.getNodeId(), TestData.EK_FMA_ID[6]);
        RelationFact f01 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek1.getNodeId());
        RelationFact f02 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek2.getNodeId());
        RelationFact f03 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek3.getNodeId());
        RelationFact f04 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek4.getNodeId());
        RelationFact f05 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek5.getNodeId());
        RelationFact f06 = dao_rFact.find(ek0.getNodeId(), rk1.getNodeId(), ek6.getNodeId());

        // TEST - Are correct RK nodes present?
        Assert.assertNotNull("Method did not return newly added node", rk1);
        Assert.assertNotNull("Method did not return newly added node ID", rk1.getNodeId());
        Assert.assertNotNull("Newly added node not found in model", rk1_copy);

        // TEST - Are correct EK nodes present?
        Assert.assertNotNull("Missing EK node 0 (pre-existing)", ek0);
        Assert.assertNotNull("Missing EK node 1 (pre-existing hanging object)", ek1);
        Assert.assertNotNull("Missing EK node 2 (pre-existing hanging subject)", ek2);
        Assert.assertNotNull("Missing EK node 3 (linked object)", ek3);
        Assert.assertNotNull("Missing EK node 4 (linked subject)", ek4);
        Assert.assertNotNull("Missing EK node 5 (new hanging object)", ek5);
        Assert.assertNotNull("Missing EK node 6 (new hanging subject)", ek6);

        // TEST - Are correct facts present?
        Assert.assertNotNull("Missing E0 -> E1", f01);
        Assert.assertNotNull("Missing E0 -> E2", f02);
        Assert.assertNotNull("Missing E0 -> E3", f03);
        Assert.assertNotNull("Missing E0 -> E4", f04);
        Assert.assertNotNull("Missing E0 -> E5", f05);
        Assert.assertNotNull("Missing E0 -> E6", f06);

        // TEST - Are Bayes Values created in the student model?
        Assert.assertEquals(7, dao_test.listAllBayes().size());
        Assert.assertNotNull("Bayes value not created for added RK", dao_bayes.get(rk1.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f01.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f02.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f03.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f04.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f05.getNodeId(), u.getId()));
        Assert.assertNotNull("Bayes value not created for fact", dao_bayes.get(f06.getNodeId(), u.getId()));
    }


    /**
     * Tests adding an RK node when that RK node already exists.
     * 
     * Key tests:
     * - Node counts do not change
     * - Pre-existing node is returned
     * - Pre-existing node is not changed.
     * - New relationships are not instantiated as facts
     * - No exception is thrown.
     * 
     * Initial model:
     * - C, CI
     * - RK: 0
     * - EK: 0 - necessary in case fact creation is attempted
     * 
     * Network:
     * - 0 -0-> 1
     * 
     * Expected model:
     * - C, CI
     * - RK: 0
     * - RK: 0
     * - No fact or EK nodes are created
     * 
     * @throws ParserException
     * @throws IOException
     */
    @Test
    @Transactional
    public void test_addRelationKnowledge_alreadyExists()
            throws ParserException, IOException
    {
        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            // Returns relationships for FMA network (see outer method)
            @Override
            public List<Relationship> retrieveRelationshipsByEntity(int fmaid)
                    throws ParserException, IOException
            {
                return new ArrayList<Relationship>();
            }
        });

        // PREP - Create test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));

        // ACT - Add RK node
        RelationKnowledge rk0_copy = facade.addRelationKnowledge(ci.getNodeId(), TestData.RK_NAME[0], TestData.RK_NAMESPACE);

        // TEST - Correct object counts
        Assert.assertEquals(1, dao_ci.count());
        Assert.assertEquals(1, dao_ek.count());
        Assert.assertEquals(1, dao_rk.count());
        Assert.assertEquals(0, dao_rFact.count());

        // TEST - Are correct RK nodes present?
        Assert.assertNotNull("Did not return pre-existing node", rk0_copy);
        Assert.assertEquals("Did not return pre-existing node", rk0_copy, rk0);
    }


    /**
     * Tests construction of a full domain model
     */
    @Test
    @Transactional
    public void test_buildFullDomainModel()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci1));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf1001 = dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf10 = dao_lFact.add(new LabelFact(ek10, lk1));
        LabelFact lf11 = dao_lFact.add(new LabelFact(ek11, lk1));

        // ACT - Build domain model
        Curriculum c_copy = facade.buildFullDomainModel(c.getNodeId());

        // TEST - Curriculum has correct children
        Assert.assertEquals(2, c_copy.getCurriculumItems().size());
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci0));
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci1));

        // PREP - Identify retrieved CIs
        Map<Long, CurriculumItem> cItems = TutoringUtils.mapifyDomainModelNodes(c_copy.getCurriculumItems());
        CurriculumItem ci0_copy = cItems.get(ci0.getNodeId());
        CurriculumItem ci1_copy = cItems.get(ci1.getNodeId());

        // TEST - CIs have correct children
        Assert.assertEquals(2, ci0_copy.getEntityKnowledge().size());
        Assert.assertEquals(2, ci0_copy.getRelationKnowledge().size());
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek00));
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek01));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk00));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk01));
        Assert.assertEquals(lk0.getNodeId(), ci0_copy.getLabelKnowledge().getNodeId());
        Assert.assertEquals(2, ci1_copy.getEntityKnowledge().size());
        Assert.assertEquals(1, ci1_copy.getRelationKnowledge().size());
        Assert.assertTrue(ci1_copy.getEntityKnowledge().contains(ek10));
        Assert.assertTrue(ci1_copy.getEntityKnowledge().contains(ek11));
        Assert.assertTrue(ci1_copy.getRelationKnowledge().contains(rk10));
        Assert.assertEquals(lk1.getNodeId(), ci1_copy.getLabelKnowledge().getNodeId());

        // PREP - Identify retrieved EKs
        Map<Long, EntityKnowledge> eks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getEntityKnowledge());
        EntityKnowledge ek00_copy = eks0.get(ek00.getNodeId());
        EntityKnowledge ek01_copy = eks0.get(ek01.getNodeId());
        Map<Long, EntityKnowledge> eks1 = TutoringUtils.mapifyDomainModelNodes(ci1_copy.getEntityKnowledge());
        EntityKnowledge ek10_copy = eks1.get(ek10.getNodeId());
        EntityKnowledge ek11_copy = eks1.get(ek11.getNodeId());

        // TEST - EKs have correct children
        Assert.assertEquals(2, ek00_copy.getRelationFacts().size());
        Assert.assertEquals(2, ek01_copy.getRelationFacts().size());
        Assert.assertEquals(1, ek10_copy.getRelationFacts().size());
        Assert.assertEquals(1, ek11_copy.getRelationFacts().size());
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek10_copy.getRelationFacts().contains(rf1001));
        Assert.assertTrue(ek11_copy.getRelationFacts().contains(rf1001));
        Assert.assertNotNull(ek00_copy.getLabelFact());
        Assert.assertNotNull(ek01_copy.getLabelFact());
        Assert.assertNotNull(ek10_copy.getLabelFact());
        Assert.assertNotNull(ek11_copy.getLabelFact());

        // PREP - Identify retrieved LKs
        LabelKnowledge lk0_copy = ci0_copy.getLabelKnowledge();
        LabelKnowledge lk1_copy = ci1_copy.getLabelKnowledge();

        // TEST - LKs have correct children
        Assert.assertEquals(2, lk0_copy.getLabelFacts().size());
        Assert.assertEquals(2, lk1_copy.getLabelFacts().size());
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf00));
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf01));
        Assert.assertTrue(lk1_copy.getLabelFacts().contains(lf10));
        Assert.assertTrue(lk1_copy.getLabelFacts().contains(lf11));

        // PREP - Identify retrieved RKs
        Map<Long, RelationKnowledge> rks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getRelationKnowledge());
        RelationKnowledge rk00_copy = rks0.get(rk00.getNodeId());
        RelationKnowledge rk01_copy = rks0.get(rk01.getNodeId());
        RelationKnowledge rk10_copy = ci1_copy.getRelationKnowledge().get(0);

        // TEST - RKs have correct children
        Assert.assertEquals(1, rk00_copy.getRelationFacts().size());
        Assert.assertEquals(1, rk01_copy.getRelationFacts().size());
        Assert.assertEquals(1, rk10_copy.getRelationFacts().size());
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(rk10_copy.getRelationFacts().contains(rf1001));
    }


    /**
     * Tests construction of a partial domain model around a relation fact
     * 
     * Network - symmetric:
     * - C: CI x2
     * - CI: EK x3, RK x2, LK
     * - RK: RF (EK0 -> EK1, EK1 -> EK2, EK2 -> EK0)
     * - EK: RF x4 (2 with RK0, 2 with RK1), LF
     * - LK: LF x3
     * Checks
     * - Returned:
     * - - C: CI0, CI1
     * - - CI0: RK00, RK01, LK0, EK00, EK01
     * - - CI1: --
     * - - EK00: RF0001, RF0200, RF0011, RF0210, LF00
     * - - EK01: RF0001, RF0102, RF0011, RF0112, LF01
     * - - EK02: --
     * - - RK00: RF0001, RF0102, RF0200
     * - - RK01: RF0011, RF0112, RF0210
     * - Not returned:
     * - - EK02, EK10, EK11, EK12, LK1, RK10, RK11
     * - - RF1001, RF1102, RF1200, RF1011, RF1112, RF1210
     * - - LF10, LF11, LF12
     */
    @Test
    @Transactional
    public void test_buildPartialDomainModel_labelFact()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        RelationKnowledge rk11 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek02 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        EntityKnowledge ek12 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0102 = dao_rFact.add(new RelationFact(ek01, rk00, ek02));
        RelationFact rf0200 = dao_rFact.add(new RelationFact(ek02, rk00, ek00));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf0112 = dao_rFact.add(new RelationFact(ek01, rk01, ek02));
        RelationFact rf0210 = dao_rFact.add(new RelationFact(ek02, rk01, ek00));
        dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        dao_rFact.add(new RelationFact(ek11, rk10, ek12));
        dao_rFact.add(new RelationFact(ek12, rk10, ek10));
        dao_rFact.add(new RelationFact(ek10, rk11, ek11));
        dao_rFact.add(new RelationFact(ek11, rk11, ek12));
        dao_rFact.add(new RelationFact(ek12, rk11, ek10));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf02 = dao_lFact.add(new LabelFact(ek02, lk0));
        dao_lFact.add(new LabelFact(ek10, lk1));
        dao_lFact.add(new LabelFact(ek11, lk1));
        dao_lFact.add(new LabelFact(ek12, lk1));

        // ACT - Build domain model
        Curriculum c_copy = facade.buildPartialDomainModel(rf0001);

        // TEST - Curriculum returned
        Assert.assertNotNull(c_copy);

        // TEST - Ancestors - C
        Assert.assertEquals(2, c_copy.getCurriculumItems().size());
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci0));
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci1));

        // TEST - CIs
        Map<Long, CurriculumItem> cItems = TutoringUtils.mapifyDomainModelNodes(c_copy.getCurriculumItems());
        CurriculumItem ci0_copy = cItems.get(ci0.getNodeId());
        CurriculumItem ci1_copy = cItems.get(ci1.getNodeId());
        // CI0
        Assert.assertEquals(2, ci0_copy.getEntityKnowledge().size());
        Assert.assertEquals(2, ci0_copy.getRelationKnowledge().size());
        Assert.assertNotNull(ci0_copy.getLabelKnowledge());
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek00));
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek01));
        Assert.assertTrue(ci0_copy.getLabelKnowledge().equals(lk0));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk00));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk01));
        // CI1
        Assert.assertEquals(0, ci1_copy.getEntityKnowledge().size());
        Assert.assertEquals(0, ci1_copy.getRelationKnowledge().size());
        Assert.assertNull(ci1_copy.getLabelKnowledge());

        // Test - EK00, EK01, EK02
        Map<Long, EntityKnowledge> eks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getEntityKnowledge());
        EntityKnowledge ek00_copy = eks0.get(ek00.getNodeId());
        EntityKnowledge ek01_copy = eks0.get(ek01.getNodeId());
        Assert.assertNotNull(ek00_copy);
        Assert.assertEquals(4, ek00_copy.getRelationFacts().size());
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0200));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0210));
        Assert.assertNotNull(ek00_copy.getLabelFact());
        Assert.assertNotNull(ek01_copy);
        Assert.assertEquals(4, ek01_copy.getRelationFacts().size());
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0102));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0112));
        Assert.assertNotNull(ek01_copy.getLabelFact());

        // TEST - LK0
        LabelKnowledge lk0_copy = ci0_copy.getLabelKnowledge();
        Assert.assertEquals(3, lk0_copy.getLabelFacts().size());
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf00));
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf01));
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf02));

        // TEST - RKs
        Map<Long, RelationKnowledge> rks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getRelationKnowledge());
        RelationKnowledge rk00_copy = rks0.get(rk00.getNodeId());
        RelationKnowledge rk01_copy = rks0.get(rk01.getNodeId());
        Assert.assertEquals(3, rk00_copy.getRelationFacts().size());
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0102));
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0200));
        Assert.assertEquals(3, rk01_copy.getRelationFacts().size());
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0112));
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0210));
    }


    /**
     * Tests construction of a partial domain model around a relation fact
     * 
     * Network - symmetric:
     * - C: CI x2
     * - CI: EK x3, RK x2, LK
     * - RK: RF x3 (EK0 -> EK1, EK1 -> EK2, EK2 -> EK0)
     * - EK: RF x4 (2 with RK0, 2 with RK1), LF
     * - LK: LF x3
     * Checks
     * - Returned:
     * - - C: CI0, CI1
     * - - CI0: RK00, RK01, LK0, EK00, EK01
     * - - CI1: --
     * - - EK00: RF0001, RF0200, RF0011, RF0210, LF00
     * - - EK01: RF0001, RF0102, RF0011, RF0112, LF01
     * - - EK02: --
     * - - LK0: LF00, LF01, LF02 - included for calculation
     * - - RK00: RF0001, RF0102, RF0200
     * - - RK01: RF0011, RF0112, RF0210 - included for calculation
     * - Not returned:
     * - - EK02, EK10, EK11, EK12, LK1, RK10, RK11
     * - - RF1001, RF1102, RF1200, RF1011, RF1112, RF1210
     * - - LF10, LF11, LF12
     */
    @Test
    @Transactional
    public void test_buildPartialDomainModel_relationFact()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        RelationKnowledge rk11 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek02 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        EntityKnowledge ek12 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0102 = dao_rFact.add(new RelationFact(ek01, rk00, ek02));
        RelationFact rf0200 = dao_rFact.add(new RelationFact(ek02, rk00, ek00));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf0112 = dao_rFact.add(new RelationFact(ek01, rk01, ek02));
        RelationFact rf0210 = dao_rFact.add(new RelationFact(ek02, rk01, ek00));
        dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        dao_rFact.add(new RelationFact(ek11, rk10, ek12));
        dao_rFact.add(new RelationFact(ek12, rk10, ek10));
        dao_rFact.add(new RelationFact(ek10, rk11, ek11));
        dao_rFact.add(new RelationFact(ek11, rk11, ek12));
        dao_rFact.add(new RelationFact(ek12, rk11, ek10));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf02 = dao_lFact.add(new LabelFact(ek02, lk0));
        dao_lFact.add(new LabelFact(ek10, lk1));
        dao_lFact.add(new LabelFact(ek11, lk1));
        dao_lFact.add(new LabelFact(ek12, lk1));

        // ACT - Build domain model
        Curriculum c_copy = facade.buildPartialDomainModel(rf0001);

        // TEST - Curriculum returned
        Assert.assertNotNull(c_copy);

        // TEST - Ancestors - C
        Assert.assertEquals(2, c_copy.getCurriculumItems().size());
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci0));
        Assert.assertTrue(c_copy.getCurriculumItems().contains(ci1));

        // TEST - CIs
        Map<Long, CurriculumItem> cItems = TutoringUtils.mapifyDomainModelNodes(c_copy.getCurriculumItems());
        CurriculumItem ci0_copy = cItems.get(ci0.getNodeId());
        CurriculumItem ci1_copy = cItems.get(ci1.getNodeId());
        // CI0
        Assert.assertEquals(2, ci0_copy.getEntityKnowledge().size());
        Assert.assertEquals(2, ci0_copy.getRelationKnowledge().size());
        Assert.assertNotNull(ci0_copy.getLabelKnowledge());
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek00));
        Assert.assertTrue(ci0_copy.getEntityKnowledge().contains(ek01));
        Assert.assertTrue(ci0_copy.getLabelKnowledge().equals(lk0));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk00));
        Assert.assertTrue(ci0_copy.getRelationKnowledge().contains(rk01));
        // CI1
        Assert.assertEquals(0, ci1_copy.getEntityKnowledge().size());
        Assert.assertEquals(0, ci1_copy.getRelationKnowledge().size());
        Assert.assertNull(ci1_copy.getLabelKnowledge());

        // Test - EK00, EK01, EK02
        Map<Long, EntityKnowledge> eks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getEntityKnowledge());
        EntityKnowledge ek00_copy = eks0.get(ek00.getNodeId());
        EntityKnowledge ek01_copy = eks0.get(ek01.getNodeId());
        Assert.assertNotNull(ek00_copy);
        Assert.assertEquals(4, ek00_copy.getRelationFacts().size());
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0200));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek00_copy.getRelationFacts().contains(rf0210));
        Assert.assertNotNull(ek00_copy.getLabelFact());
        Assert.assertNotNull(ek01_copy);
        Assert.assertEquals(4, ek01_copy.getRelationFacts().size());
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0102));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(ek01_copy.getRelationFacts().contains(rf0112));
        Assert.assertNotNull(ek01_copy.getLabelFact());

        // TEST - LK0
        LabelKnowledge lk0_copy = ci0_copy.getLabelKnowledge();
        Assert.assertEquals(3, lk0_copy.getLabelFacts().size());
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf00));
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf01));
        Assert.assertTrue(lk0_copy.getLabelFacts().contains(lf02));

        // TEST - RKs
        Map<Long, RelationKnowledge> rks0 = TutoringUtils.mapifyDomainModelNodes(ci0_copy.getRelationKnowledge());
        RelationKnowledge rk00_copy = rks0.get(rk00.getNodeId());
        RelationKnowledge rk01_copy = rks0.get(rk01.getNodeId());
        Assert.assertEquals(3, rk00_copy.getRelationFacts().size());
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0001));
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0102));
        Assert.assertTrue(rk00_copy.getRelationFacts().contains(rf0200));
        Assert.assertEquals(3, rk01_copy.getRelationFacts().size());
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0011));
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0112));
        Assert.assertTrue(rk01_copy.getRelationFacts().contains(rf0210));
    }


    @Test
    @Transactional
    public void test_computePartialStudentModel_labelFact()
    {
        // PREP - Create test network
        Curriculum c = dao_c.add(new Curriculum("name1", "creator1", "desc1"));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        RelationKnowledge rk11 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek02 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        EntityKnowledge ek12 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0102 = dao_rFact.add(new RelationFact(ek01, rk00, ek02));
        RelationFact rf0200 = dao_rFact.add(new RelationFact(ek02, rk00, ek00));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf0112 = dao_rFact.add(new RelationFact(ek01, rk01, ek02));
        RelationFact rf0210 = dao_rFact.add(new RelationFact(ek02, rk01, ek00));
        RelationFact rf1001 = dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        RelationFact rf1102 = dao_rFact.add(new RelationFact(ek11, rk10, ek12));
        RelationFact rf1200 = dao_rFact.add(new RelationFact(ek12, rk10, ek10));
        RelationFact rf1011 = dao_rFact.add(new RelationFact(ek10, rk11, ek11));
        RelationFact rf1112 = dao_rFact.add(new RelationFact(ek11, rk11, ek12));
        RelationFact rf1210 = dao_rFact.add(new RelationFact(ek12, rk11, ek10));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf10 = dao_lFact.add(new LabelFact(ek10, lk1));
        LabelFact lf11 = dao_lFact.add(new LabelFact(ek11, lk1));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // PREP - Assign initial P values
        List<BayesValue> bayesValues = new ArrayList<BayesValue>();
        bayesValues.add(new BayesValue(c.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rf0001.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf0102.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf0200.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(rf0011.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(rf0112.getNodeId(), u.getId(), 0.5, new Date()));
        bayesValues.add(new BayesValue(rf0210.getNodeId(), u.getId(), 0.6, new Date()));
        bayesValues.add(new BayesValue(rf1001.getNodeId(), u.getId(), 0.7, new Date()));
        bayesValues.add(new BayesValue(rf1102.getNodeId(), u.getId(), 0.8, new Date()));
        bayesValues.add(new BayesValue(rf1200.getNodeId(), u.getId(), 0.9, new Date()));
        bayesValues.add(new BayesValue(rf1011.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf1112.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf1210.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(lf00.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(lf01.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(lf10.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(lf11.getNodeId(), u.getId(), 0.5, new Date()));
        dao_bayes.setMultiple(bayesValues);

        // PREP - Compute Student Model
        facade.computeStudentModel(u.getId(), c.getNodeId());

        // ACT - Adjust evidence
        dao_bayes.set(lf00.getNodeId(), u.getId(), 0.9, new Date());
        dao_bayes.set(lf11.getNodeId(), u.getId(), 0.9, new Date()); // This evidence should not be recomputed

        // ACT - Adjust partial student model
        facade.computePartialStudentModel(u.getId(), lf00);

        // TEST - Validate new Bayes values
        Assert.assertEquals(0.458, dao_bayes.get(c.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.400, dao_bayes.get(ci0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.487, dao_bayes.get(ci1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.460, dao_bayes.get(ek00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.280, dao_bayes.get(ek01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.480, dao_bayes.get(ek10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.460, dao_bayes.get(ek11.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.550, dao_bayes.get(lk0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.450, dao_bayes.get(lk1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.200, dao_bayes.get(rk00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.500, dao_bayes.get(rk01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.800, dao_bayes.get(rk10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.200, dao_bayes.get(rk11.getNodeId(), u.getId()).getP(), 0.001);
    }


    @Test
    @Transactional
    public void test_computePartialStudentModel_relationFact()
    {
        // PREP - Create test network
        Curriculum c = dao_c.add(new Curriculum("name1", "creator1", "desc1"));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        RelationKnowledge rk11 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek02 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        EntityKnowledge ek12 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0102 = dao_rFact.add(new RelationFact(ek01, rk00, ek02));
        RelationFact rf0200 = dao_rFact.add(new RelationFact(ek02, rk00, ek00));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf0112 = dao_rFact.add(new RelationFact(ek01, rk01, ek02));
        RelationFact rf0210 = dao_rFact.add(new RelationFact(ek02, rk01, ek00));
        RelationFact rf1001 = dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        RelationFact rf1102 = dao_rFact.add(new RelationFact(ek11, rk10, ek12));
        RelationFact rf1200 = dao_rFact.add(new RelationFact(ek12, rk10, ek10));
        RelationFact rf1011 = dao_rFact.add(new RelationFact(ek10, rk11, ek11));
        RelationFact rf1112 = dao_rFact.add(new RelationFact(ek11, rk11, ek12));
        RelationFact rf1210 = dao_rFact.add(new RelationFact(ek12, rk11, ek10));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf10 = dao_lFact.add(new LabelFact(ek10, lk1));
        LabelFact lf11 = dao_lFact.add(new LabelFact(ek11, lk1));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // PREP - Assign initial P values
        List<BayesValue> bayesValues = new ArrayList<BayesValue>();
        bayesValues.add(new BayesValue(c.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rf0001.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf0102.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf0200.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(rf0011.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(rf0112.getNodeId(), u.getId(), 0.5, new Date()));
        bayesValues.add(new BayesValue(rf0210.getNodeId(), u.getId(), 0.6, new Date()));
        bayesValues.add(new BayesValue(rf1001.getNodeId(), u.getId(), 0.7, new Date()));
        bayesValues.add(new BayesValue(rf1102.getNodeId(), u.getId(), 0.8, new Date()));
        bayesValues.add(new BayesValue(rf1200.getNodeId(), u.getId(), 0.9, new Date()));
        bayesValues.add(new BayesValue(rf1011.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf1112.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf1210.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(lf00.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(lf01.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(lf10.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(lf11.getNodeId(), u.getId(), 0.5, new Date()));
        dao_bayes.setMultiple(bayesValues);

        // PREP - Compute Student Model
        facade.computeStudentModel(u.getId(), c.getNodeId());

        // // ACT - Adjust evidence
        dao_bayes.set(rf0001.getNodeId(), u.getId(), 0.9, new Date());
        dao_bayes.set(rf1001.getNodeId(), u.getId(), 0.1, new Date()); // This evidence should not be recomputed

        // ACT - Adjust partial student model
        facade.computePartialStudentModel(u.getId(), rf0001);

        // TEST - Validate new Bayes values
        Assert.assertEquals(0.458, dao_bayes.get(c.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.400, dao_bayes.get(ci0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.487, dao_bayes.get(ci1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.460, dao_bayes.get(ek00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.440, dao_bayes.get(ek01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.480, dao_bayes.get(ek10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.460, dao_bayes.get(ek11.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.150, dao_bayes.get(lk0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.450, dao_bayes.get(lk1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.467, dao_bayes.get(rk00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.500, dao_bayes.get(rk01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.800, dao_bayes.get(rk10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.200, dao_bayes.get(rk11.getNodeId(), u.getId()).getP(), 0.001);
    }


    @Test
    @Transactional
    public void test_computeStudentModel()
    {
        // PREP - Create test network
        Curriculum c = dao_c.add(new Curriculum("name1", "creator1", "desc1"));
        CurriculumItem ci0 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        CurriculumItem ci1 = dao_ci.add(new CurriculumItem(TestData.CI_NAME[1], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[1], c));
        RelationKnowledge rk00 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci0));
        RelationKnowledge rk01 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci0));
        RelationKnowledge rk10 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci1));
        RelationKnowledge rk11 = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[1], ci1));
        LabelKnowledge lk0 = dao_lk.add(new LabelKnowledge(ci0));
        LabelKnowledge lk1 = dao_lk.add(new LabelKnowledge(ci1));
        EntityKnowledge ek00 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci0));
        EntityKnowledge ek01 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci0));
        EntityKnowledge ek02 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek10 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci1));
        EntityKnowledge ek11 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci1));
        EntityKnowledge ek12 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        RelationFact rf0001 = dao_rFact.add(new RelationFact(ek00, rk00, ek01));
        RelationFact rf0102 = dao_rFact.add(new RelationFact(ek01, rk00, ek02));
        RelationFact rf0200 = dao_rFact.add(new RelationFact(ek02, rk00, ek00));
        RelationFact rf0011 = dao_rFact.add(new RelationFact(ek00, rk01, ek01));
        RelationFact rf0112 = dao_rFact.add(new RelationFact(ek01, rk01, ek02));
        RelationFact rf0210 = dao_rFact.add(new RelationFact(ek02, rk01, ek00));
        RelationFact rf1001 = dao_rFact.add(new RelationFact(ek10, rk10, ek11));
        RelationFact rf1102 = dao_rFact.add(new RelationFact(ek11, rk10, ek12));
        RelationFact rf1200 = dao_rFact.add(new RelationFact(ek12, rk10, ek10));
        RelationFact rf1011 = dao_rFact.add(new RelationFact(ek10, rk11, ek11));
        RelationFact rf1112 = dao_rFact.add(new RelationFact(ek11, rk11, ek12));
        RelationFact rf1210 = dao_rFact.add(new RelationFact(ek12, rk11, ek10));
        LabelFact lf00 = dao_lFact.add(new LabelFact(ek00, lk0));
        LabelFact lf01 = dao_lFact.add(new LabelFact(ek01, lk0));
        LabelFact lf10 = dao_lFact.add(new LabelFact(ek10, lk1));
        LabelFact lf11 = dao_lFact.add(new LabelFact(ek11, lk1));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // PREP - Assign initial P values
        List<BayesValue> bayesValues = new ArrayList<BayesValue>();
        bayesValues.add(new BayesValue(c.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ci1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(ek11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk00.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk01.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk10.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rk11.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk0.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(lk1.getNodeId(), u.getId(), 0.0, new Date()));
        bayesValues.add(new BayesValue(rf0001.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf0102.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf0200.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(rf0011.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(rf0112.getNodeId(), u.getId(), 0.5, new Date()));
        bayesValues.add(new BayesValue(rf0210.getNodeId(), u.getId(), 0.6, new Date()));
        bayesValues.add(new BayesValue(rf1001.getNodeId(), u.getId(), 0.7, new Date()));
        bayesValues.add(new BayesValue(rf1102.getNodeId(), u.getId(), 0.8, new Date()));
        bayesValues.add(new BayesValue(rf1200.getNodeId(), u.getId(), 0.9, new Date()));
        bayesValues.add(new BayesValue(rf1011.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(rf1112.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(rf1210.getNodeId(), u.getId(), 0.3, new Date()));
        bayesValues.add(new BayesValue(lf00.getNodeId(), u.getId(), 0.1, new Date()));
        bayesValues.add(new BayesValue(lf01.getNodeId(), u.getId(), 0.2, new Date()));
        bayesValues.add(new BayesValue(lf10.getNodeId(), u.getId(), 0.4, new Date()));
        bayesValues.add(new BayesValue(lf11.getNodeId(), u.getId(), 0.5, new Date()));
        dao_bayes.setMultiple(bayesValues);

        // ACT - Compute Student Model
        facade.computeStudentModel(u.getId(), c.getNodeId());

        // TEST - Validate new Bayes values
        Assert.assertEquals(0.425, dao_bayes.get(c.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.300, dao_bayes.get(ci0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.487, dao_bayes.get(ci1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.300, dao_bayes.get(ek00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.280, dao_bayes.get(ek01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.480, dao_bayes.get(ek10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.460, dao_bayes.get(ek11.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.150, dao_bayes.get(lk0.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.450, dao_bayes.get(lk1.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.200, dao_bayes.get(rk00.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.500, dao_bayes.get(rk01.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.800, dao_bayes.get(rk10.getNodeId(), u.getId()).getP(), 0.001);
        Assert.assertEquals(0.200, dao_bayes.get(rk11.getNodeId(), u.getId()).getP(), 0.001);
    }


    /**
     * Test facade deletion of a curriculum.
     * 
     * Check:
     * - Graph nodes for curriculum and child objects are deleted
     * - Bayes values are deleted
     */
    @Test
    @Transactional
    public void test_deleteCurriculum()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        RelationFact rf = dao_rFact.add(new RelationFact(ek, rk, ek));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf = dao_lFact.add(new LabelFact(ek, lk));

        dao_bayes.set(c.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(ci.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(ek.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(rk.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(rf.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(lk.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(lf.getNodeId(), 1, 0.5, new Date());

        // ACT - Delete Curriculum
        facade.deleteCurriculum(c);

        // TEST - has curriculum node been deleted?
        Assert.assertEquals(0, dao_c.count());
        Assert.assertEquals(0, dao_ci.count());
        Assert.assertEquals(0, dao_ek.count());
        Assert.assertEquals(0, dao_rk.count());
        Assert.assertEquals(0, dao_rFact.count());
        Assert.assertEquals(0, dao_lk.count());
        Assert.assertEquals(0, dao_lFact.count());
        Assert.assertEquals(0, dao_test.listAllBayes().size());
    }


    /**
     * Test facade deletion of a curriculum item. Ensure both the graph node and any associated Bayes Values are
     * deleted.
     */
    @Test
    @Transactional
    public void test_deleteCurriculumItem()
    {
        // PREP - Prepare test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        RelationFact rf = dao_rFact.add(new RelationFact(ek, rk, ek));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf = dao_lFact.add(new LabelFact(ek, lk));

        dao_bayes.set(c.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(ci.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(ek.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(rk.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(rf.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(lk.getNodeId(), 1, 0.5, new Date());
        dao_bayes.set(lf.getNodeId(), 1, 0.5, new Date());

        // ACT - Delete Curriculum Item
        facade.deleteCurriculumItem(ci);

        // TEST - has curriculum node been deleted?
        Assert.assertEquals(1, dao_c.count());
        Assert.assertEquals(0, dao_ci.count());
        Assert.assertEquals(0, dao_ek.count());
        Assert.assertEquals(0, dao_rk.count());
        Assert.assertEquals(0, dao_rFact.count());
        Assert.assertEquals(0, dao_lk.count());
        Assert.assertEquals(0, dao_lFact.count());
        Assert.assertEquals(1, dao_test.listAllBayes().size());
    }


    /**
     * Tests deleteEntityKnowledge
     * 
     * Uses hub and spoke ontology with both hanging and linked entities:
     * - Linked entities: E0, E1, E3, E5
     * - Hanging entities: E2, E4
     * - E0 -> E1
     * - E0 -> E2
     * - E3 -> E0
     * - E4 -> E0
     * 
     * Steps:
     * - Create test objects
     * - Delete hub entity (E0)
     * - Verify counts
     * - Verify:
     * --- E0 remains as hanging EK
     * --- E1, E3 remain linked
     * --- E2, E4 are gone
     * --- E5 is unchanged
     * 
     */
    @Test
    @Transactional
    public void test_deleteEntityKnowledge()
    {
        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        EntityKnowledge ek4 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], null));
        EntityKnowledge ek5 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5], ci));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        RelationFact rf01 = dao_rFact.add(new RelationFact(ek0, rk, ek1));
        RelationFact rf02 = dao_rFact.add(new RelationFact(ek0, rk, ek2));
        RelationFact rf30 = dao_rFact.add(new RelationFact(ek3, rk, ek0));
        RelationFact rf40 = dao_rFact.add(new RelationFact(ek4, rk, ek0));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf0 = dao_lFact.add(new LabelFact(ek0, lk));
        LabelFact lf1 = dao_lFact.add(new LabelFact(ek1, lk));
        LabelFact lf2 = dao_lFact.add(new LabelFact(ek2, lk)); // Creating LF for hanging EK to test cleanup
        LabelFact lf3 = dao_lFact.add(new LabelFact(ek3, lk));
        LabelFact lf4 = dao_lFact.add(new LabelFact(ek4, lk)); // Creating LF for hanging EK to test cleanup
        LabelFact lf5 = dao_lFact.add(new LabelFact(ek5, lk));

        // PREP - Create test bayes values
        dao_bayes.set(ci.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek1.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek2.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek3.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek4.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek5.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rk.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf01.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf02.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf30.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf40.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lk.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf1.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf2.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf3.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf4.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf5.getNodeId(), 1, 0.0, new Date());

        // ACT - Perform deletion
        facade.deleteEntityKnowledge(ek0);

        // TEST - Correct object counts
        Assert.assertEquals(4, dao_ek.count());
        Assert.assertEquals(2, dao_rFact.count());
        Assert.assertEquals(3, dao_lFact.count()); // E1, E3, E5
        Assert.assertEquals(11, dao_test.listAllBayes().size());

        // TEST - EKs not eligible for deletion not deleted
        Assert.assertNotNull("EK 0 missing (should be hanging)", dao_ek.get(ek0.getNodeId()));
        Assert.assertNotNull("EK 1 missing (should remain)", dao_ek.get(ek1.getNodeId()));
        Assert.assertNotNull("EK 3 missing (should remain)", dao_ek.get(ek3.getNodeId()));
        Assert.assertNotNull("EK 5 missing (should be unchanged)", dao_ek.get(ek5.getNodeId()));

        // TEST - EKs remain linked correctly
        Assert.assertTrue("EK 1 not linked (should remain linked)", dao_ek.get(ek1.getNodeId()).isLinked());
        Assert.assertTrue("EK 3 not linked (should remain linked)", dao_ek.get(ek3.getNodeId()).isLinked());
        Assert.assertTrue("EK 5 not linked (should remain linked)", dao_ek.get(ek5.getNodeId()).isLinked());

        // TEST - Correct relation facts remain
        List<RelationFact> rFacts = dao_rFact.listByCurriculumItem(ci.getNodeId());
        Assert.assertTrue(rFacts.contains(rf01));
        Assert.assertFalse(rFacts.contains(rf02));
        Assert.assertTrue(rFacts.contains(rf30));
        Assert.assertFalse(rFacts.contains(rf40));

        // TEST - Correct label facts remain
        List<LabelFact> lFacts = dao_lFact.listByCurriculumItem(ci.getNodeId());
        Assert.assertFalse(lFacts.contains(lf0));
        Assert.assertTrue(lFacts.contains(lf1));
        Assert.assertFalse(lFacts.contains(lf2));
        Assert.assertTrue(lFacts.contains(lf3));
        Assert.assertFalse(lFacts.contains(lf4));
        Assert.assertTrue(lFacts.contains(lf5));

        // TEST - Correct Bayes Values remain
        Map<Long, BayesValue> bayes = TutoringUtils.mapifyBayesValuesByDomainId(dao_test.listAllBayes());
        Assert.assertNotNull(bayes.get(ci.getNodeId()));
        Assert.assertNull(bayes.get(ek0.getNodeId()));
        Assert.assertNotNull(bayes.get(ek1.getNodeId()));
        Assert.assertNull(bayes.get(ek2.getNodeId()));
        Assert.assertNotNull(bayes.get(ek3.getNodeId()));
        Assert.assertNull(bayes.get(ek4.getNodeId()));
        Assert.assertNotNull(bayes.get(ek5.getNodeId()));
        Assert.assertNotNull(bayes.get(rk.getNodeId()));
        Assert.assertNotNull(bayes.get(rf01.getNodeId()));
        Assert.assertNull(bayes.get(rf02.getNodeId()));
        Assert.assertNotNull(bayes.get(rf30.getNodeId()));
        Assert.assertNull(bayes.get(rf40.getNodeId()));
        Assert.assertNotNull(bayes.get(lk.getNodeId()));
        Assert.assertNull(bayes.get(lf0.getNodeId()));
        Assert.assertNotNull(bayes.get(lf1.getNodeId()));
        Assert.assertNull(bayes.get(lf2.getNodeId()));
        Assert.assertNotNull(bayes.get(lf3.getNodeId()));
        Assert.assertNull(bayes.get(lf4.getNodeId()));
        Assert.assertNotNull(bayes.get(lf5.getNodeId()));
    }


    /**
     * Test .delectLabelKnowledge
     * 
     * Check
     * - LabelKnowledge node is deleted
     * - LabelFacts are deleted
     * - Associated Bayes Values are deleted
     * - No other nodes are deleted
     */
    @Test
    @Transactional
    public void test_deleteLabelKnowledge()
    {
        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        RelationKnowledge rk = dao_rk.add(new RelationKnowledge(TestData.RK_STRING[0], ci));
        RelationFact rf01 = dao_rFact.add(new RelationFact(ek0, rk, ek1));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf0 = dao_lFact.add(new LabelFact(ek0, lk));
        LabelFact lf1 = dao_lFact.add(new LabelFact(ek1, lk));

        // PREP - Create test bayes values
        dao_bayes.set(ci.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek1.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rk.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf01.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lk.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(lf1.getNodeId(), 1, 0.0, new Date());

        // ACT - Perform deletion
        facade.deleteLabelKnowledge(lk);

        // TEST - Correct object counts
        Assert.assertEquals(2, dao_ek.count());
        Assert.assertEquals(0, dao_lk.count());
        Assert.assertEquals(0, dao_lFact.count());
        Assert.assertEquals(1, dao_rFact.count());
        Assert.assertEquals(5, dao_test.listAllBayes().size());

        // TEST - Correct Bayes Values remain
        Map<Long, BayesValue> bayes = TutoringUtils.mapifyBayesValuesByDomainId(dao_test.listAllBayes());
        Assert.assertNotNull(bayes.get(ci.getNodeId()));
        Assert.assertNotNull(bayes.get(ek0.getNodeId()));
        Assert.assertNotNull(bayes.get(ek1.getNodeId()));
        Assert.assertNotNull(bayes.get(rk.getNodeId()));
        Assert.assertNotNull(bayes.get(rf01.getNodeId()));
        Assert.assertNull(bayes.get(lk.getNodeId()));
        Assert.assertNull(bayes.get(lf0.getNodeId()));
        Assert.assertNull(bayes.get(lf1.getNodeId()));
    }


    /**
     * Tests testDeleteRelationKnowledge
     * 
     * Uses hub and spoke ontology with both hanging and linked entities:
     * - Linked entities: E0, E1, E3, E5
     * - Hanging entities: E2, E4
     * - E0 -R0-> E1
     * - E0 -R0-> E2
     * - E3 -R0-> E0
     * - E4 -R0-> E0
     * - E5 -R1-> E0
     * 
     * Steps:
     * - Create test objects
     * - Delete R0
     * - Verify counts
     * - Verify:
     * --- E0 remains as hanging EK
     * --- E1, E3 remain linked
     * --- E2, E4 are gone
     * --- E5 is unchanged
     */
    @Test
    @Transactional
    public void test_deleteRelationKnowledge()
    {
        // PREP - Prepare test objects
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0]));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], null));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        EntityKnowledge ek4 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[4], TestData.EK_FMA_LABEL[4], null));
        EntityKnowledge ek5 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[5], TestData.EK_FMA_LABEL[5], ci));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci));
        RelationKnowledge rk1 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[1], TestData.RK_NAMESPACE, ci));
        RelationFact rf001 = dao_rFact.add(new RelationFact(ek0, rk0, ek1));
        RelationFact rf002 = dao_rFact.add(new RelationFact(ek0, rk0, ek2));
        RelationFact rf300 = dao_rFact.add(new RelationFact(ek3, rk0, ek0));
        RelationFact rf400 = dao_rFact.add(new RelationFact(ek4, rk0, ek0));
        RelationFact rf510 = dao_rFact.add(new RelationFact(ek5, rk1, ek0));

        // PREP - Create test bayes values
        dao_bayes.set(ci.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek1.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek2.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek3.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek4.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(ek5.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rk0.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rk1.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf001.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf002.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf300.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf400.getNodeId(), 1, 0.0, new Date());
        dao_bayes.set(rf510.getNodeId(), 1, 0.0, new Date());

        // ACT - Perform deletion
        facade.deleteRelationKnowledge(rk0);

        // TEST - Correct object count
        Assert.assertEquals(4, dao_ek.count());
        Assert.assertEquals(1, dao_rk.count());
        Assert.assertEquals(1, dao_rFact.count());
        Assert.assertEquals(7, dao_test.listAllBayes().size());

        // TEST - EKs not eligible for deletion not deleted
        Assert.assertNotNull("EK 0 missing (should remain)", dao_ek.get(ek0.getNodeId()));
        Assert.assertNotNull("EK 1 missing (should remain)", dao_ek.get(ek1.getNodeId()));
        Assert.assertNotNull("EK 3 missing (should remain)", dao_ek.get(ek3.getNodeId()));
        Assert.assertNotNull("EK 5 missing (should be unchanged)", dao_ek.get(ek5.getNodeId()));

        // TEST - Pre-linked EKs remain linked
        Assert.assertTrue("EK 0 not linked (should remain linked)", dao_ek.get(ek0.getNodeId()).isLinked());
        Assert.assertTrue("EK 1 not linked (should remain linked)", dao_ek.get(ek1.getNodeId()).isLinked());
        Assert.assertTrue("EK 3 not linked (should remain linked)", dao_ek.get(ek3.getNodeId()).isLinked());
        Assert.assertTrue("EK 5 not linked (should remain linked)", dao_ek.get(ek5.getNodeId()).isLinked());

        // TEST - Correct Bayes Values remain
        Map<Long, BayesValue> bayes = TutoringUtils.mapifyBayesValuesByDomainId(dao_test.listAllBayes());
        Assert.assertNotNull(bayes.get(ci.getNodeId()));
        Assert.assertNotNull(bayes.get(ek0.getNodeId()));
        Assert.assertNotNull(bayes.get(ek1.getNodeId()));
        Assert.assertNull(bayes.get(ek2.getNodeId())); // Cleaned up - hanging nodes should not have BVs
        Assert.assertNotNull(bayes.get(ek3.getNodeId()));
        Assert.assertNull(bayes.get(ek4.getNodeId())); // Cleaned up - hanging nodes should not have BVs
        Assert.assertNotNull(bayes.get(ek5.getNodeId()));
        Assert.assertNull(bayes.get(rk0.getNodeId()));
        Assert.assertNotNull(bayes.get(rk1.getNodeId()));
        Assert.assertNull(bayes.get(rf001.getNodeId()));
        Assert.assertNull(bayes.get(rf002.getNodeId()));
        Assert.assertNull(bayes.get(rf300.getNodeId()));
        Assert.assertNull(bayes.get(rf400.getNodeId()));
        Assert.assertNotNull(bayes.get(rf510.getNodeId()));
    }


    @Test
    @Transactional
    public void test_enrolStudentInCurriculum() throws IOException, ParserException
    {
        // PREP - Create test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci));
        RelationKnowledge rk1 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[1], TestData.RK_NAMESPACE, ci));
        RelationFact rf001 = dao_rFact.add(new RelationFact(ek0, rk0, ek1));
        RelationFact rf112 = dao_rFact.add(new RelationFact(ek1, rk1, ek2));
        RelationFact rf213 = dao_rFact.add(new RelationFact(ek2, rk1, ek3));
        RelationFact rf300 = dao_rFact.add(new RelationFact(ek3, rk0, ek0));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf0 = dao_lFact.add(new LabelFact(ek0, lk));
        LabelFact lf1 = dao_lFact.add(new LabelFact(ek1, lk));
        LabelFact lf2 = dao_lFact.add(new LabelFact(ek2, lk));
        LabelFact lf3 = dao_lFact.add(new LabelFact(ek3, lk));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // TEST - Ensure no Bayes Values are created before enrolment
        Assert.assertEquals(0, dao_test.listAllBayes().size());

        // Act - Enrol student
        facade.enrolStudentInCurriculum(u.getId(), c.getNodeId());

        // TEST - Correct Bayes Values created
        Map<Long, BayesValue> bayesMap = TutoringUtils.mapifyBayesValuesByDomainId(dao_test.listAllBayes());
        Assert.assertNotNull(bayesMap.get(c.getNodeId()));
        Assert.assertNotNull(bayesMap.get(ci.getNodeId()));
        Assert.assertNotNull(bayesMap.get(ek0.getNodeId()));
        Assert.assertNotNull(bayesMap.get(ek1.getNodeId()));
        Assert.assertNotNull(bayesMap.get(ek2.getNodeId()));
        Assert.assertNotNull(bayesMap.get(ek3.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rk0.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rk1.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rf001.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rf112.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rf213.getNodeId()));
        Assert.assertNotNull(bayesMap.get(rf300.getNodeId()));
        Assert.assertNotNull(bayesMap.get(lk.getNodeId()));
        Assert.assertNotNull(bayesMap.get(lf0.getNodeId()));
        Assert.assertNotNull(bayesMap.get(lf1.getNodeId()));
        Assert.assertNotNull(bayesMap.get(lf2.getNodeId()));
        Assert.assertNotNull(bayesMap.get(lf3.getNodeId()));

        // TEST - Bayes values have correct student ID
        Assert.assertEquals(u.getId(), bayesMap.get(c.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(ci.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(ek0.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(ek1.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(ek2.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(ek3.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rk0.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rk1.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rf001.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rf112.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rf213.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(rf300.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(lk.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(lf0.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(lf1.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(lf2.getNodeId()).getStudentId());
        Assert.assertEquals(u.getId(), bayesMap.get(lf3.getNodeId()).getStudentId());

        // TEST - No other Bayes Values created
        Assert.assertEquals(17, dao_test.listAllBayes().size());

        // TEST - Enrolment record created
        Assert.assertTrue(dao_enrolment.isEnrolled(c.getNodeId(), u.getId()));
    }


    /**
     * Check:
     * - Curriculum BV is deleted
     * - CI BVs are deleted
     * - Linked EK BVs are deleted
     * - Hanging EK BVs are deleted
     * - RK BVs are deleted
     * - Fact BVs are deleted
     */
    @Test
    @Transactional
    public void test_unenrolStudentFromCurriculum()
    {
        // PREP - Create test objects
        Curriculum c = dao_c.add(new Curriculum(TestData.C_NAME[0], TestData.C_CREATOR_NAME, TestData.C_DESCRIPTION));
        CurriculumItem ci = dao_ci.add(new CurriculumItem(TestData.CI_NAME[0], TestData.CI_DESCRIPTION, TestData.CI_WEIGHT[0], c));
        EntityKnowledge ek0 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[0], TestData.EK_FMA_LABEL[0], ci));
        EntityKnowledge ek1 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[1], TestData.EK_FMA_LABEL[1], ci));
        EntityKnowledge ek2 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[2], TestData.EK_FMA_LABEL[2], ci));
        EntityKnowledge ek3 = dao_ek.add(new EntityKnowledge(TestData.EK_FMA_ID[3], TestData.EK_FMA_LABEL[3], ci));
        RelationKnowledge rk0 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[0], TestData.RK_NAMESPACE, ci));
        RelationKnowledge rk1 = dao_rk.add(new RelationKnowledge(TestData.RK_NAME[1], TestData.RK_NAMESPACE, ci));
        RelationFact rf001 = dao_rFact.add(new RelationFact(ek0, rk0, ek1));
        RelationFact rf112 = dao_rFact.add(new RelationFact(ek1, rk1, ek2));
        RelationFact rf213 = dao_rFact.add(new RelationFact(ek2, rk1, ek3));
        RelationFact rf300 = dao_rFact.add(new RelationFact(ek3, rk0, ek0));
        LabelKnowledge lk = dao_lk.add(new LabelKnowledge(ci));
        LabelFact lf0 = dao_lFact.add(new LabelFact(ek0, lk));
        LabelFact lf1 = dao_lFact.add(new LabelFact(ek1, lk));
        LabelFact lf2 = dao_lFact.add(new LabelFact(ek2, lk));
        LabelFact lf3 = dao_lFact.add(new LabelFact(ek3, lk));
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // PREP - Create Bayes Values
        dao_bayes.set(c.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(ci.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(ek0.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(ek1.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(ek2.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(ek3.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rk0.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rk1.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rf001.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rf112.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rf213.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(rf300.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(lk.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(lf0.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(lf1.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(lf2.getNodeId(), u.getId(), 0.2, new Date());
        dao_bayes.set(lf3.getNodeId(), u.getId(), 0.2, new Date());

        // PREP - Prepare enrolment
        dao_enrolment.add(new Enrolment(c.getNodeId(), u.getId()));

        // TEST - Everything is set up properly
        Assert.assertEquals(17, dao_test.listAllBayes().size());

        // ACT - Unenrol student
        facade.unenrolStudentFromCurriculum(u.getId(), c.getNodeId());

        // TEST - Correct Bayes Values created
        Map<Long, BayesValue> bayesMap = TutoringUtils.mapifyBayesValuesByDomainId(dao_test.listAllBayes());
        Assert.assertNull(bayesMap.get(c.getNodeId()));
        Assert.assertNull(bayesMap.get(ci.getNodeId()));
        Assert.assertNull(bayesMap.get(ek0.getNodeId()));
        Assert.assertNull(bayesMap.get(ek1.getNodeId()));
        Assert.assertNull(bayesMap.get(ek2.getNodeId()));
        Assert.assertNull(bayesMap.get(ek3.getNodeId()));
        Assert.assertNull(bayesMap.get(rk0.getNodeId()));
        Assert.assertNull(bayesMap.get(rk1.getNodeId()));
        Assert.assertNull(bayesMap.get(rf001.getNodeId()));
        Assert.assertNull(bayesMap.get(rf112.getNodeId()));
        Assert.assertNull(bayesMap.get(rf213.getNodeId()));
        Assert.assertNull(bayesMap.get(rf300.getNodeId()));
        Assert.assertNull(bayesMap.get(lk.getNodeId()));
        Assert.assertNull(bayesMap.get(lf0.getNodeId()));
        Assert.assertNull(bayesMap.get(lf1.getNodeId()));
        Assert.assertNull(bayesMap.get(lf2.getNodeId()));
        Assert.assertNull(bayesMap.get(lf3.getNodeId()));

        // TEST - Bayes network for student correctly removed
        Assert.assertEquals(0, dao_test.listAllBayes().size());

        // TEST - Enrolment record removed
        Assert.assertFalse(dao_enrolment.isEnrolled(c.getNodeId(), u.getId()));
    }
}
