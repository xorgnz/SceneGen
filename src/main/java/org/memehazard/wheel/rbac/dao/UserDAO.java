package org.memehazard.wheel.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.rbac.model.User;


public interface UserDAO extends MyBatisDAO
{
    public void add(User obj);


    public void assignToUserGroup(@Param("userId") long userId, @Param("userGroupId") long userGroupId);


    public void deassignFromUserGroup(@Param("userId") long userId, @Param("userGroupId") long userGroupId);


    public void delete(long id);


    public void deleteAll();


    public User get(long id);


    public User getByEmail(String email);


    public User getByUsername(String username);


    public List<User> listAll();


    public List<User> listByRole(long roleId);


    public List<User> listByUserGroup(long userGroupId);


    public void update(User obj);
}
