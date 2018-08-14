package org.memehazard.wheel.rbac.test;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface RbacTestDAO extends MyBatisDAO
{
    public int countRolePermLinksByPermission(int id);


    public int countRolePermLinksByRole(int id);


    public int countUserRoleLinksByRole(int id);


    public int countUserRoleLinksByUser(int id);


    public int countUserGroupUserLinksByUser(int id);


    public int countUserGroupUserLinksByUserGroup(int id);


    public void createTestData();
}
