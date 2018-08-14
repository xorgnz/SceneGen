package org.memehazard.wheel.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.rbac.model.Permission;


public interface PermissionDAO extends MyBatisDAO
{
    public void add(Permission obj);


    public void assignToRole(@Param("permissionId") int permissionId, @Param("roleId") int roleId);


    public void delete(int id);


    public void deassignFromRole(@Param("permissionId") int permissionId, @Param("roleId") int roleId);


    public Permission get(int id);


    public Permission getByName(String name);


    public List<Permission> listAll();


    public List<Permission> listByRole(int roleId);


    public void update(Permission obj);
}
