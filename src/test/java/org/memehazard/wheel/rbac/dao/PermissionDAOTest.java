package org.memehazard.wheel.rbac.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.model.Permission;
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
public class PermissionDAOTest
{
    @SuppressWarnings("unused")
    private Logger        log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionDAO dao;
    @Autowired
    private RoleDAO       dao_role;
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
        Permission p = new Permission(
                TestData.PERM_NAME[0] + " alt",
                TestData.PERM_DESCRIPTION + " alt");
        dao.add(p);

        // Test - Did we get an object ID?
        Assert.assertNotNull(p.getId());

        // Test - does it show in list all?
        List<Permission> perms = dao.listAll();
        Assert.assertEquals(5, perms.size());

        // Test - is it correct?
        Permission p_copy = dao.get(p.getId());
        Assert.assertEquals(TestData.PERM_NAME[0] + " alt", p_copy.getName());
        Assert.assertEquals(TestData.PERM_DESCRIPTION + " alt", p_copy.getDescription());
    }


    @Test
    @Transactional
    public void test_assignToRole()
    {
        // Act
        dao.assignToRole(4, 1);

        // Test - is correct number of permissions linked to role?
        Assert.assertEquals(4, dao.listByRole(1).size());
    }


    @Test
    @Transactional
    public void test_deassignFromRole()
    {
        // Act
        dao.deassignFromRole(1, 1);

        // Test - is correct number of permissions linked to role?
        Assert.assertEquals(2, dao.listByRole(1).size());
    }


    @Test
    @Transactional
    public void test_delete()
    {
        // Act - by ID
        dao.delete(1);

        // Test - is it gone?
        Assert.assertEquals(3, dao.listAll().size());

        // Test whether links are deleted by cascade
        Assert.assertEquals(0, dao_test.countRolePermLinksByPermission(1));
    }


    @Test
    @Transactional
    public void test_get()
    {
        // Act
        Permission p = dao.get(1);

        // Test - did we get an object back?
        Assert.assertNotNull(p);

        // Test - does that object have expected values?
        Assert.assertEquals(1, p.getId());
        Assert.assertEquals(TestData.PERM_NAME[0], p.getName());
        Assert.assertEquals(TestData.PERM_DESCRIPTION, p.getDescription());
    }


    @Test
    @Transactional
    public void test_getByName()
    {
        // Act
        Permission p = dao.getByName(TestData.PERM_NAME[3]);

        // Test - did we get an object back?
        Assert.assertNotNull(p);

        // Test - does that object have expected values?
        Assert.assertEquals(4, p.getId());
        Assert.assertEquals(TestData.PERM_NAME[3], p.getName());
        Assert.assertEquals(TestData.PERM_DESCRIPTION, p.getDescription());
    }


    @Test
    @Transactional
    public void test_listAll()
    {
        // Act
        List<Permission> perms = dao.listAll();

        // Test - are all objects retrieved?
        Assert.assertEquals(4, perms.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.PERM_NAME[3], perms.get(0).getName());
        Assert.assertEquals(TestData.PERM_NAME[2], perms.get(1).getName());
        Assert.assertEquals(TestData.PERM_NAME[1], perms.get(2).getName());
        Assert.assertEquals(TestData.PERM_NAME[0], perms.get(3).getName());
    }


    @Test
    @Transactional
    public void test_listByRole()
    {
        List<Permission> perms = dao.listByRole(1);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, perms.size());

        // Test - are objects ordered by name?
        Assert.assertEquals(TestData.PERM_NAME[2], perms.get(0).getName());
        Assert.assertEquals(TestData.PERM_NAME[1], perms.get(1).getName());
        Assert.assertEquals(TestData.PERM_NAME[0], perms.get(2).getName());
    }


    @Test
    @Transactional
    public void test_update()
    {
        // Act
        Permission p0 = dao.get(1);
        p0.setName(TestData.PERM_NAME[0] + " alt");
        p0.setDescription(TestData.PERM_DESCRIPTION + " alt");
        dao.update(p0);

        // Test - have changes been made?
        List<Permission> perms = dao.listAll();
        Permission p1 = perms.get(3);
        Assert.assertEquals(1, p1.getId());
        Assert.assertEquals(TestData.PERM_NAME[0] + " alt", p1.getName());
        Assert.assertEquals(TestData.PERM_DESCRIPTION + " alt", p1.getDescription());
    }
}
