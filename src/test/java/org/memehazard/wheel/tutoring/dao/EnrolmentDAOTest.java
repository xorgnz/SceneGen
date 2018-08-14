package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.model.Enrolment;
import org.memehazard.wheel.tutoring.model.Event;
import org.memehazard.wheel.tutoring.test.TestData;
import org.memehazard.wheel.tutoring.test.TutoringTestMyBatisDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class EnrolmentDAOTest
{
    @Autowired
    private EnrolmentDAO           dao;
    @Autowired
    private EventDAO               dao_event;
    @Autowired
    private TutoringTestMyBatisDAO dao_test;
    @Autowired
    private UserDAO                dao_user;


    @BeforeTransaction
    public void before()
    {
        dao_test.deleteAll();
    }


    @Test(expected = DuplicateKeyException.class)
    @Transactional
    public void test_add_duplicate()
    {
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // ACT - Attempt to add an existing enrolment
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u.getId()));
    }


    @Test
    @Transactional
    public void test_add_isEnrolled_list_delete()
    {
        // PREP - Create test objects
        User u = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        dao_user.add(u);

        // ACT - Add enrolment
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u.getId()));

        // TEST - Correct # of enrolments exist
        Assert.assertEquals(2, dao_test.listAllEnrolments().size());

        // TEST - Enrolment checkable
        Assert.assertTrue(dao.isEnrolled(TestData.ITSEV_CURRICULUM_ID[0], u.getId()));
        Assert.assertTrue(dao.isEnrolled(TestData.ITSEV_CURRICULUM_ID[1], u.getId()));
        Assert.assertFalse(dao.isEnrolled(TestData.ITSEV_CURRICULUM_ID[2], u.getId()));
        Assert.assertFalse(dao.isEnrolled(TestData.ITSEV_CURRICULUM_ID[0], u.getId() + 5));

        // ACT - delete a record
        dao.delete(TestData.ITSEV_CURRICULUM_ID[0], u.getId());

        // TEST - Correct # of enrolments exist
        Assert.assertEquals(1, dao_test.listAllEnrolments().size());
        Assert.assertFalse(dao.isEnrolled(10, u.getId()));
    }


    @Test
    @Transactional
    public void test_listByCurriculum()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        User u2 = new User(TestData.USER_USERNAME[2], TestData.USER_PASSWORD, TestData.USER_EMAIL[2], TestData.USER_FNAME[2], TestData.USER_LNAME[2]);
        dao_user.add(u0);
        dao_user.add(u1);
        dao_user.add(u2);
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u2.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u1.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[1], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));

        // ACT - Obtain lists
        List<Enrolment> c10Enrolments = dao.listByCurriculum(TestData.ITSEV_CURRICULUM_ID[0]);
        List<Enrolment> c11Enrolments = dao.listByCurriculum(TestData.ITSEV_CURRICULUM_ID[1]);
        List<Enrolment> c12Enrolments = dao.listByCurriculum(TestData.ITSEV_CURRICULUM_ID[2]);

        // TEST - Correct number of students in lists
        Assert.assertEquals(3, c10Enrolments.size());
        Assert.assertEquals(1, c11Enrolments.size());
        Assert.assertEquals(0, c12Enrolments.size());

        // TEST - Correct students returned
        Assert.assertEquals(TestData.USER_LNAME[0], c10Enrolments.get(0).getStudent().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[1], c10Enrolments.get(1).getStudent().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[2], c10Enrolments.get(2).getStudent().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[0], c11Enrolments.get(0).getStudent().getLastName());

        // TEST - Correct Event counts returned
        Assert.assertEquals(2, c10Enrolments.get(0).getEventCount());
        Assert.assertEquals(1, c10Enrolments.get(1).getEventCount());
        Assert.assertEquals(0, c10Enrolments.get(2).getEventCount());
        Assert.assertEquals(1, c11Enrolments.get(0).getEventCount());
    }


    @Test
    @Transactional
    public void test_listByStudent()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        User u2 = new User(TestData.USER_USERNAME[2], TestData.USER_PASSWORD, TestData.USER_EMAIL[2], TestData.USER_FNAME[2], TestData.USER_LNAME[2]);
        dao_user.add(u0);
        dao_user.add(u1);
        dao_user.add(u2);
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[2], u0.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[1], u0.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));
        dao_event.add(new Event(TestData.ITSEV_CURRICULUM_ID[0], u1.getId(), TestData.ITSEV_DESCRIPTION[0], TestData.ITSEV_SOURCE[0]));

        // ACT - Obtain lists
        List<Enrolment> s0Enrolments = dao.listByStudent(u0.getId());
        List<Enrolment> s1Enrolments = dao.listByStudent(u1.getId());
        List<Enrolment> s2Enrolments = dao.listByStudent(u2.getId());

        // TEST - Correct number of students in lists
        Assert.assertEquals(3, s0Enrolments.size());
        Assert.assertEquals(1, s1Enrolments.size());
        Assert.assertEquals(0, s2Enrolments.size());

        // TEST - Correct students returned
        Assert.assertEquals(TestData.ITSEV_CURRICULUM_ID[0], s0Enrolments.get(0).getCurriculumId());
        Assert.assertEquals(TestData.ITSEV_CURRICULUM_ID[1], s0Enrolments.get(1).getCurriculumId());
        Assert.assertEquals(TestData.ITSEV_CURRICULUM_ID[2], s0Enrolments.get(2).getCurriculumId());
        Assert.assertEquals(TestData.ITSEV_CURRICULUM_ID[0], s1Enrolments.get(0).getCurriculumId());

        // TEST - Correct Event counts returned
        Assert.assertEquals(2, s0Enrolments.get(0).getEventCount());
        Assert.assertEquals(1, s0Enrolments.get(1).getEventCount());
        Assert.assertEquals(0, s0Enrolments.get(2).getEventCount());
        Assert.assertEquals(1, s1Enrolments.get(0).getEventCount());
    }


    @Test
    @Transactional
    public void test_listStudentIdsByCurriculum()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        User u2 = new User(TestData.USER_USERNAME[2], TestData.USER_PASSWORD, TestData.USER_EMAIL[2], TestData.USER_FNAME[2], TestData.USER_LNAME[2]);
        dao_user.add(u0);
        dao_user.add(u1);
        dao_user.add(u2);
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u2.getId()));
        dao.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()));

        // ACT - Obtain lists
        List<Long> c10Students = dao.listStudentIdsByCurriculum(TestData.ITSEV_CURRICULUM_ID[0]);
        List<Long> c11Students = dao.listStudentIdsByCurriculum(TestData.ITSEV_CURRICULUM_ID[1]);
        List<Long> c12Students = dao.listStudentIdsByCurriculum(TestData.ITSEV_CURRICULUM_ID[2]);

        // TEST - Correct number of students in lists
        Assert.assertEquals(3, c10Students.size());
        Assert.assertEquals(1, c11Students.size());
        Assert.assertEquals(0, c12Students.size());

        // TEST - Correct students returned
        Assert.assertTrue(c10Students.contains(u0.getId()));
        Assert.assertTrue(c10Students.contains(u1.getId()));
        Assert.assertTrue(c10Students.contains(u2.getId()));
        Assert.assertTrue(c11Students.contains(u0.getId()));
    }
}
