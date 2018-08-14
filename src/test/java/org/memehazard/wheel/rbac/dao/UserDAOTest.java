package org.memehazard.wheel.rbac.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.test.RbacTestDAO;
import org.memehazard.wheel.rbac.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class UserDAOTest
{
    @SuppressWarnings("unused")
    private Logger       log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDAO      dao;

    @Autowired
    private UserGroupDAO dao_usergrp;

    @Autowired
    private RoleDAO      dao_role;
    @Autowired
    public RbacTestDAO   dao_test;


    @Before
    public void before()
    {
        dao_test.createTestData();
    }


    @Test
    @Transactional
    public void test_add()
    {
        // Act
        User u = new User(
                TestData.USER_USERNAME[0] + " alt",
                TestData.USER_PASSWORD + " alt",
                TestData.USER_EMAIL[0] + ".alt",
                TestData.USER_FNAME[0] + " alt",
                TestData.USER_LNAME[0] + " alt");
        dao.add(u);

        // Test - Did we get an object ID?
        Assert.assertNotNull(u.getId());

        // Test - does it show in list all?
        Assert.assertEquals(5, dao.listAll().size());

        // Test - is it correct?
        User u_copy = dao.get(u.getId());
        Assert.assertEquals(TestData.USER_USERNAME[0] + " alt", u_copy.getUsername());
        Assert.assertEquals(TestData.USER_EMAIL[0] + ".alt", u_copy.getEmail());
        Assert.assertEquals(TestData.USER_FNAME[0] + " alt", u_copy.getFirstName());
        Assert.assertEquals(TestData.USER_LNAME[0] + " alt", u_copy.getLastName());
    }


    @Test
    @Transactional
    public void test_assignToGroup()
    {
        // Act
        dao.assignToUserGroup(4, 1);

        // Test - is correct number of users linked to group?
        Assert.assertEquals(4, dao.listByUserGroup(1).size());
    }


    @Test
    @Transactional
    public void test_deassignFromGroup()
    {
        // Act
        dao.deassignFromUserGroup(1, 1);

        // Test - is correct number of users linked to group?
        Assert.assertEquals(2, dao.listByUserGroup(1).size());
    }


    @Test
    @Transactional
    public void test_delete()
    {
        // Act - Delete user
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(3, dao.listAll().size());

        // Test - are links deleted by cascade?
        Assert.assertEquals(0, dao_test.countUserRoleLinksByUser(1));
        Assert.assertEquals(0, dao_test.countUserGroupUserLinksByUser(1));
    }


    @Test
    @Transactional
    public void test_get()
    {
        // Act
        User u = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(u);

        // Test - does that object have expected values?
        Assert.assertEquals(1, u.getId());
        Assert.assertEquals(TestData.USER_USERNAME[0], u.getUsername());
        Assert.assertEquals(TestData.USER_PASSWORD, u.getPassword());
        Assert.assertEquals(TestData.USER_FNAME[0], u.getFirstName());
        Assert.assertEquals(TestData.USER_LNAME[0], u.getLastName());
        Assert.assertEquals(TestData.USER_EMAIL[0], u.getEmail());
    }


    @Test
    @Transactional
    public void test_getByUsername()
    {
        // Act
        User u = dao.getByUsername(TestData.USER_USERNAME[1]);

        // Test - did we get an object back?
        Assert.assertNotNull(u);

        // Test - does that object have expected values?
        Assert.assertEquals(2, u.getId());
        Assert.assertEquals(TestData.USER_USERNAME[1], u.getUsername());
        Assert.assertEquals(TestData.USER_PASSWORD, u.getPassword());
        Assert.assertEquals(TestData.USER_FNAME[1], u.getFirstName());
        Assert.assertEquals(TestData.USER_LNAME[1], u.getLastName());
        Assert.assertEquals(TestData.USER_EMAIL[1], u.getEmail());
    }


    @Test
    @Transactional
    public void test_getByEmail()
    {
        // Act
        User u = dao.getByUsername(TestData.USER_USERNAME[2]);

        // Test - did we get an object back?
        Assert.assertNotNull(u);

        // Test - does that object have expected values?
        Assert.assertEquals(3, u.getId());
        Assert.assertEquals(TestData.USER_USERNAME[2], u.getUsername());
        Assert.assertEquals(TestData.USER_PASSWORD, u.getPassword());
        Assert.assertEquals(TestData.USER_FNAME[2], u.getFirstName());
        Assert.assertEquals(TestData.USER_LNAME[2], u.getLastName());
        Assert.assertEquals(TestData.USER_EMAIL[2], u.getEmail());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // Act
        List<User> users = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, users.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.USER_LNAME[3], users.get(0).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[2], users.get(1).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[1], users.get(2).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[0], users.get(3).getLastName());
        Assert.assertEquals(TestData.USER_FNAME[3], users.get(0).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[2], users.get(1).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[1], users.get(2).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[0], users.get(3).getFirstName());
    }


    @Test
    @Transactional
    public void test_listByRole()
    {
        // Act
        List<User> users = dao.listByRole(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, users.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.USER_LNAME[2], users.get(0).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[1], users.get(1).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[0], users.get(2).getLastName());
        Assert.assertEquals(TestData.USER_FNAME[2], users.get(0).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[1], users.get(1).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[0], users.get(2).getFirstName());
    }


    @Test
    @Transactional
    public void test_listByUserGroup()
    {
        // Act
        List<User> users = dao.listByUserGroup(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, users.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.USER_LNAME[2], users.get(0).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[1], users.get(1).getLastName());
        Assert.assertEquals(TestData.USER_LNAME[0], users.get(2).getLastName());
        Assert.assertEquals(TestData.USER_FNAME[2], users.get(0).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[1], users.get(1).getFirstName());
        Assert.assertEquals(TestData.USER_FNAME[0], users.get(2).getFirstName());
    }


    @Test
    @Transactional
    public void test_update()
    {
        // Act
        User u = dao.get(1);
        u.setUsername(TestData.USER_USERNAME + " alt2");
        u.setPassword(TestData.USER_PASSWORD + " alt");
        u.setEmail(TestData.USER_EMAIL[0] + ".alt");
        u.setFirstName(TestData.USER_FNAME[0] + " alt");
        u.setLastName(TestData.USER_LNAME[0] + " alt");
        dao.update(u);

        // Test - have changes been made?
        User u_copy = dao.get(1);
        Assert.assertEquals(1, u_copy.getId());
        Assert.assertEquals(TestData.USER_USERNAME + " alt2", u_copy.getUsername());
        Assert.assertEquals(TestData.USER_PASSWORD + " alt", u_copy.getPassword());
        Assert.assertEquals(TestData.USER_EMAIL[0] + ".alt", u_copy.getEmail());
        Assert.assertEquals(TestData.USER_FNAME[0] + " alt", u_copy.getFirstName());
        Assert.assertEquals(TestData.USER_LNAME[0] + " alt", u_copy.getLastName());
    }
}
