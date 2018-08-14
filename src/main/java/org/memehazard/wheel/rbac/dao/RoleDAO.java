package org.memehazard.wheel.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.rbac.model.Role;

public interface RoleDAO extends MyBatisDAO
{
    public void add(Role obj);


    public void assignToUser(@Param("roleId") long roleId, @Param("userId") long userId);


    public void deassignFromUser(@Param("roleId") long roleId, @Param("userId") long userId);


    public void delete(int id);


    public void deleteAll();


    public Role get(int id);


    public Role getByName(String name);


    public List<Role> listAll();


    public List<Role> listByPermission(int permId);


    public List<Role> listByUser(int userId);


    public void update(Role obj);
}
