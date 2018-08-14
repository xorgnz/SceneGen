package org.memehazard.wheel.rbac.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.model.Role;
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
public class RoleDAOTest
{
    @SuppressWarnings("unused")
    private Logger        log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleDAO       dao;
    @Autowired
    private PermissionDAO dao_permission;
    @Autowired
    private UserDAO       dao_user;
    @Autowired
    public RbacTestDAO    dao_test;


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
        Role r = new Role(
                TestData.ROLE_NAME[0] + " alt",
                TestData.ROLE_DESCRIPTION + " alt");
        dao.add(r);

        // Test - Did we get an object ID?
        Assert.assertNotNull(r.getId());

        // Test - does it show in list all?
        Assert.assertEquals(5, dao.listAll().size());

        // Test - is it correct?
        Role r_copy = dao.get(r.getId());
        Assert.assertEquals(TestData.ROLE_NAME[0] + " alt", r_copy.getName());
        Assert.assertEquals(TestData.ROLE_DESCRIPTION + " alt", r_copy.getDescription());
    }


    @Test
    @Transactional
    public void test_assignToUser()
    {
        // Act
        dao.assignToUser(4, 1);

        // Test - is correct number of roles linked to user?
        Assert.assertEquals(4, dao.listByUser(1).size());
    }


    @Test
    @Transactional
    public void test_deassignFromUser()
    {
        // Act
        dao.deassignFromUser(1, 1);

        // Test - is correct number of roles linked to user?
        Assert.assertEquals(2, dao.listByUser(1).size());
    }


    @Test
    @Transactional
    public void test_delete()
    {
        // Delete role from DB
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(3, dao.listAll().size());

        // Test whether links are deleted by cascade
        Assert.assertEquals(0, dao_test.countRolePermLinksByRole(1));
        Assert.assertEquals(0, dao_test.countUserRoleLinksByRole(1));
    }


    @Test
    @Transactional
    public void test_get()
    {
        // Act
        Role r = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(r);

        // Test - does that object have expected values?
        Assert.assertEquals(1, r.getId());
        Assert.assertEquals(TestData.ROLE_NAME[0], r.getName());
        Assert.assertEquals(TestData.ROLE_DESCRIPTION, r.getDescription());
    }


    @Test
    @Transactional
    public void test_getByName()
    {
        // Act
        Role r = dao.getByName(TestData.ROLE_NAME[3]);

        // Test - did we get an object back?
        Assert.assertNotNull(r);

        // Test - does that object have expected values?
        Assert.assertEquals(4, r.getId());
        Assert.assertEquals(TestData.ROLE_NAME[3], r.getName());
        Assert.assertEquals(TestData.ROLE_DESCRIPTION, r.getDescription());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // Act
        List<Role> roles = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, roles.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ROLE_NAME[3], roles.get(0).getName());
        Assert.assertEquals(TestData.ROLE_NAME[2], roles.get(1).getName());
        Assert.assertEquals(TestData.ROLE_NAME[1], roles.get(2).getName());
        Assert.assertEquals(TestData.ROLE_NAME[0], roles.get(3).getName());
    }


    @Test
    @Transactional
    public void test_listByPermission()
    {
        List<Role> roles = dao.listByPermission(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, roles.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ROLE_NAME[2], roles.get(0).getName());
        Assert.assertEquals(TestData.ROLE_NAME[1], roles.get(1).getName());
        Assert.assertEquals(TestData.ROLE_NAME[0], roles.get(2).getName());
    }


    @Test
    @Transactional
    public void test_listByUser()
    {
        List<Role> roles = dao.listByUser(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, roles.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.ROLE_NAME[2], roles.get(0).getName());
        Assert.assertEquals(TestData.ROLE_NAME[1], roles.get(1).getName());
        Assert.assertEquals(TestData.ROLE_NAME[0], roles.get(2).getName());
    }


    @Test
    @Transactional
    public void test_update()
    {
        // Act
        Role r = dao.get(1);
        r.setName(TestData.ROLE_NAME[0] + " alt");
        r.setDescription(TestData.ROLE_DESCRIPTION + " alt");
        dao.update(r);

        // Test - have changes been made?
        Role r_copy = dao.get(1);
        Assert.assertEquals(1, r_copy.getId());
        Assert.assertEquals(TestData.ROLE_NAME[0] + " alt", r_copy.getName());
        Assert.assertEquals(TestData.ROLE_DESCRIPTION + " alt", r_copy.getDescription());
    }
}
