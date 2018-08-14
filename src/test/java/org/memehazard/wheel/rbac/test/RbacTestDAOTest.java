package org.memehazard.wheel.rbac.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class RbacTestDAOTest
{
    @SuppressWarnings("unused")
    private Logger     log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RbacTestDAO dao_test;


    @Before
    public void before()
    {
        dao_test.createTestData();
    }


    @Test
    @Transactional
    public void test_testUtilities()
    {
        // Role <-> Permission links
        Assert.assertEquals(3, dao_test.countRolePermLinksByPermission(1));
        Assert.assertEquals(1, dao_test.countRolePermLinksByPermission(2));
        Assert.assertEquals(1, dao_test.countRolePermLinksByPermission(3));
        Assert.assertEquals(0, dao_test.countRolePermLinksByPermission(4));
        Assert.assertEquals(3, dao_test.countRolePermLinksByRole(1));
        Assert.assertEquals(1, dao_test.countRolePermLinksByRole(2));
        Assert.assertEquals(1, dao_test.countRolePermLinksByRole(3));
        Assert.assertEquals(0, dao_test.countRolePermLinksByRole(4));

        // User <-> Role links
        Assert.assertEquals(3, dao_test.countUserRoleLinksByRole(1));
        Assert.assertEquals(1, dao_test.countUserRoleLinksByRole(2));
        Assert.assertEquals(1, dao_test.countUserRoleLinksByRole(3));
        Assert.assertEquals(0, dao_test.countUserRoleLinksByRole(4));
        Assert.assertEquals(3, dao_test.countUserRoleLinksByUser(1));
        Assert.assertEquals(1, dao_test.countUserRoleLinksByUser(2));
        Assert.assertEquals(1, dao_test.countUserRoleLinksByUser(3));
        Assert.assertEquals(0, dao_test.countUserRoleLinksByUser(4));

        // User Group <-> User links
        Assert.assertEquals(3, dao_test.countUserGroupUserLinksByUser(1));
        Assert.assertEquals(1, dao_test.countUserGroupUserLinksByUser(2));
        Assert.assertEquals(1, dao_test.countUserGroupUserLinksByUser(3));
        Assert.assertEquals(0, dao_test.countUserGroupUserLinksByUser(4));
        Assert.assertEquals(3, dao_test.countUserGroupUserLinksByUserGroup(1));
        Assert.assertEquals(1, dao_test.countUserGroupUserLinksByUserGroup(2));
        Assert.assertEquals(1, dao_test.countUserGroupUserLinksByUserGroup(3));
        Assert.assertEquals(0, dao_test.countUserGroupUserLinksByUserGroup(4));
    }
}

