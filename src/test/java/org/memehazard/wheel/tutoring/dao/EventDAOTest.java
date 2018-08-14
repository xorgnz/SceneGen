package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.model.Event;
import org.memehazard.wheel.tutoring.test.TestData;
import org.memehazard.wheel.tutoring.test.TutoringTestMyBatisDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class EventDAOTest
{
    @Autowired
    private EventDAO               dao;

    @Autowired
    private TutoringTestMyBatisDAO dao_test;

    @Autowired
    private UserDAO                dao_user;

    @SuppressWarnings("unused")
    private Logger                 log = LoggerFactory.getLogger(this.getClass());


    @BeforeTransaction
    public void before()
    {
        dao_test.deleteAll();
    }


    @Test
    @Transactional
    public void test_addGetDelete()
    {
        // PREP - Create test objects
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);
        Event e = new Event(TestData.ITSEV_CURRICULUM_ID[0], u.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]);
        e.setDeltaP(TestData.ITSEV_P_ASSERTION[0]);
        e.setSubjectEntityId(TestData.ITSEV_SUBJECT_ENTITY_ID[0]);
        e.setRelation(TestData.ITSEV_RELATION[0]);
        e.setObjectEntityId(TestData.ITSEV_OBJECT_ENTITY_ID[0]);
        e.setValue(TestData.ITSEV_VALUE[0]);

        // ACT - Add
        dao.add(e);

        // TEST - Was ID returned?
        Assert.assertNotNull(e.getId());

        // ACT - Get
        Event e_copy = dao.get(e.getId());

        // TEST - Was correct object returned?
        Assert.assertNotNull(e_copy);
        Assert.assertEquals(e.getId(), e_copy.getId());
        Assert.assertEquals(e.getCurriculumId(), e_copy.getCurriculumId());
        Assert.assertEquals(e.getObjectEntityId(), e_copy.getObjectEntityId());
        Assert.assertEquals(e.getDeltaP(), e_copy.getDeltaP(), 0.01);
        Assert.assertEquals(e.getRelation(), e_copy.getRelation());
        Assert.assertEquals(e.getSource(), e_copy.getSource());
        Assert.assertEquals(e.getStudentId(), e_copy.getStudentId());
        Assert.assertEquals(e.getSubjectEntityId(), e_copy.getSubjectEntityId());
        Assert.assertEquals(e.getTimestamp(), e_copy.getTimestamp());
        Assert.assertEquals(e.getValue(), e_copy.getValue());

        // ACT - Delete
        dao.delete(e.getId());

        // TEST - Object deleted?
        Assert.assertNull(dao.get(e.getId()));
    }


    @Test
    @Transactional
    public void test_deleteByStudentAndCurriculum()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        dao_user.add(u0);
        dao_user.add(u1);
        Event e001 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), "desc001", "src001");
        Event e002 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), "desc002", "src002");
        Event e101 = new Event(TestData.ITSEV_CURRICULUM_ID[1], u0.getId(), "desc101", "src101");
        Event e011 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u1.getId(), "desc011", "src011");
        dao.add(e001);
        dao.add(e002);
        dao.add(e101);
        dao.add(e011);

        // TEST - Pre-Counts correct
        Assert.assertEquals(2, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()).size());
        Assert.assertEquals(1, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()).size());
        Assert.assertEquals(1, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()).size());
        Assert.assertEquals(0, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u1.getId()).size());

        // ACT - Delete
        dao.deleteByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u1.getId());

        // TEST - Counts correct
        Assert.assertEquals(2, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()).size());
        Assert.assertEquals(1, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()).size());
        Assert.assertEquals(0, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()).size());
        Assert.assertEquals(0, dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u1.getId()).size());
    }


    @Test
    @Transactional
    public void test_listByCurriculumAndStudent()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        dao_user.add(u0);
        dao_user.add(u1);
        Event e001 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), "desc001", "src001");
        Event e002 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), "desc002", "src002");
        Event e101 = new Event(TestData.ITSEV_CURRICULUM_ID[1], u0.getId(), "desc101", "src101");
        Event e011 = new Event(TestData.ITSEV_CURRICULUM_ID[0], u1.getId(), "desc011", "src011");
        dao.add(e001);
        dao.add(e002);
        dao.add(e101);
        dao.add(e011);

        // ACT - List events
        List<Event> events00 = dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u0.getId());
        List<Event> events10 = dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u0.getId());
        List<Event> events01 = dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[0], u1.getId());
        List<Event> events11 = dao.listByCurriculumAndStudent(TestData.ITSEV_CURRICULUM_ID[1], u1.getId());

        // TEST - Counts correct
        Assert.assertEquals(2, events00.size());
        Assert.assertEquals(1, events10.size());
        Assert.assertEquals(1, events01.size());
        Assert.assertEquals(0, events11.size());
    }


    @Test
    @Transactional
    public void test_update()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        dao_user.add(u0);
        dao_user.add(u1);
        Event e = new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]);
        e.setDeltaP(TestData.ITSEV_P_ASSERTION[0]);
        e.setSubjectEntityId(TestData.ITSEV_SUBJECT_ENTITY_ID[0]);
        e.setRelation(TestData.ITSEV_RELATION[0]);
        e.setObjectEntityId(TestData.ITSEV_OBJECT_ENTITY_ID[0]);
        e.setValue(TestData.ITSEV_VALUE[0]);
        dao.add(e);

        // ACT - Update object
        e.setCurriculumId(TestData.ITSEV_CURRICULUM_ID[1]);
        e.setObjectEntityId(TestData.ITSEV_OBJECT_ENTITY_ID[1]);
        e.setDeltaP(TestData.ITSEV_P_ASSERTION[1]);
        e.setRelation(TestData.ITSEV_RELATION[1]);
        e.setSource(TestData.ITSEV_SOURCE[1]);
        e.setStudentId(u1.getId());
        e.setSubjectEntityId(TestData.ITSEV_SUBJECT_ENTITY_ID[1]);
        e.setTimestamp(TestData.ITSEV_TIMESTAMPS(1).getTime());
        e.setValue(TestData.ITSEV_VALUE[1]);
        dao.update(e);

        // TEST - Object updated?
        Event e_copy = dao.get(e.getId());
        Assert.assertEquals(e.getId(), e_copy.getId());
        Assert.assertEquals(e.getCurriculumId(), e_copy.getCurriculumId());
        Assert.assertEquals(e.getObjectEntityId(), e_copy.getObjectEntityId());
        Assert.assertEquals(e.getDeltaP(), e_copy.getDeltaP(), 0.01);
        Assert.assertEquals(e.getRelation(), e_copy.getRelation());
        Assert.assertEquals(e.getSource(), e_copy.getSource());
        Assert.assertEquals(e.getStudentId(), e_copy.getStudentId());
        Assert.assertEquals(e.getSubjectEntityId(), e_copy.getSubjectEntityId());
        Assert.assertEquals(e.getTimestamp(), e_copy.getTimestamp());
        Assert.assertEquals(e.getValue(), e_copy.getValue());
    }
}
