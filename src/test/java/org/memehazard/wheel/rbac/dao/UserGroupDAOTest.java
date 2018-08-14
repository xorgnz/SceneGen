package org.memehazard.wheel.rbac.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.model.UserGroup;
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
public class UserGroupDAOTest
{
    @SuppressWarnings("unused")
    private Logger                log                 = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserGroupDAO          dao;

    @Autowired
    private UserDAO               dao_user;
    @Autowired
    public RbacTestDAO            dao_test;


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
        UserGroup ug = new UserGroup(
                TestData.USERGRP_NAME[0] + " alt",
                TestData.USERGRP_DESCRIPTION + " alt");
        dao.add(ug);

        // Test - Did we get an object ID?
        Assert.assertNotNull(ug.getId());

        // Test - does it show in list all?
        Assert.assertEquals(5, dao.listAll().size());

        // Test - is it correct?
        UserGroup ug1 = dao.get(ug.getId());
        Assert.assertEquals(TestData.USERGRP_NAME[0] + " alt", ug1.getName());
        Assert.assertEquals(TestData.USERGRP_DESCRIPTION + " alt", ug1.getDescription());
    }


    @Test
    @Transactional
    public void test_delete()
    {
        // Act - Delete User Group
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(3, dao.listAll().size());

        // Test whether links are deleted by cascade
        Assert.assertEquals(0, dao_test.countUserGroupUserLinksByUserGroup(1));
    }


    @Test
    @Transactional
    public void test_get()
    {
        // Act
        UserGroup ug = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(ug);

        // Test - does that object have expected values?
        Assert.assertEquals(1, ug.getId());
        Assert.assertEquals(TestData.USERGRP_NAME[0], ug.getName());
        Assert.assertEquals(TestData.USERGRP_DESCRIPTION, ug.getDescription());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // Act
        List<UserGroup> groups = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, groups.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.USERGRP_NAME[3], groups.get(0).getName());
        Assert.assertEquals(TestData.USERGRP_NAME[2], groups.get(1).getName());
        Assert.assertEquals(TestData.USERGRP_NAME[1], groups.get(2).getName());
        Assert.assertEquals(TestData.USERGRP_NAME[0], groups.get(3).getName());
    }


    @Test
    @Transactional
    public void test_listByUser()
    {
        // Act
        List<UserGroup> groups = dao.listByUser(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, groups.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.USERGRP_NAME[2], groups.get(0).getName());
        Assert.assertEquals(TestData.USERGRP_NAME[1], groups.get(1).getName());
        Assert.assertEquals(TestData.USERGRP_NAME[0], groups.get(2).getName());
    }


    @Test
    @Transactional
    public void test_update()
    {
        // Act
        UserGroup ug = dao.get(1);
        ug.setName(TestData.USERGRP_NAME[0] + " alt");
        ug.setDescription(TestData.USERGRP_DESCRIPTION + " alt");
        dao.update(ug);

        // Test - have changes been made?
        UserGroup ug_copy = dao.get(1);
        Assert.assertEquals(1, ug_copy.getId());
        Assert.assertEquals(TestData.USERGRP_NAME[0] + " alt", ug_copy.getName());
        Assert.assertEquals(TestData.USERGRP_DESCRIPTION + " alt", ug_copy.getDescription());
    }
}
