package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.tutoring.model.Enrolment;
import org.memehazard.wheel.tutoring.model.Student;
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
public class StudentDAOTest
{
    @Autowired
    private StudentDAO             dao;

    @Autowired
    private EnrolmentDAO           dao_enrolment;

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
    public void test_listAll()
    {
        // PREP - Create test objects
        User u0 = new User(TestData.USER_USERNAME[0], TestData.USER_PASSWORD, TestData.USER_EMAIL[0], TestData.USER_FNAME[0], TestData.USER_LNAME[0]);
        User u1 = new User(TestData.USER_USERNAME[1], TestData.USER_PASSWORD, TestData.USER_EMAIL[1], TestData.USER_FNAME[1], TestData.USER_LNAME[1]);
        User u2 = new User(TestData.USER_USERNAME[2], TestData.USER_PASSWORD, TestData.USER_EMAIL[2], TestData.USER_FNAME[2], TestData.USER_LNAME[2]);
        User u3 = new User(TestData.USER_USERNAME[3], TestData.USER_PASSWORD, TestData.USER_EMAIL[3], TestData.USER_FNAME[3], TestData.USER_LNAME[3]);
        dao_user.add(u0);
        dao_user.add(u1);
        dao_user.add(u2);
        dao_user.add(u3);
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u0.getId()));
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u0.getId()));
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u1.getId()));
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[1], u1.getId()));
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u2.getId()));
        dao_enrolment.add(new Enrolment(TestData.ITSEV_CURRICULUM_ID[0], u3.getId()));

        // ACT - Update object
        List<Student> students = dao.listAll();

        // TEST - Correct count?
        Assert.assertEquals(4, students.size());

        // TEST - Objects correctly ordered?
        Assert.assertEquals(TestData.USER_LNAME[0], students.get(0).getUser().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[1], students.get(1).getUser().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[2], students.get(2).getUser().getLastName());
        Assert.assertEquals(TestData.USER_LNAME[3], students.get(3).getUser().getLastName());
        Assert.assertEquals(TestData.USER_FNAME[0], students.get(0).getUser().getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[1], students.get(1).getUser().getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[2], students.get(2).getUser().getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[3], students.get(3).getUser().getFirstName());

        // TEST - Enrolment counts correct?
        Assert.assertEquals(2, students.get(0).getEnrolments().size());
        Assert.assertEquals(2, students.get(1).getEnrolments().size());
        Assert.assertEquals(1, students.get(2).getEnrolments().size());
        Assert.assertEquals(1, students.get(3).getEnrolments().size());
    }
}
