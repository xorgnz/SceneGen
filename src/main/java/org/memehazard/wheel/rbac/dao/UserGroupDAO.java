package org.memehazard.wheel.rbac.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.rbac.model.UserGroup;

public interface UserGroupDAO extends MyBatisDAO
{
    public void add(UserGroup obj);


    public void delete(int id);


    public void deleteAll();


    public UserGroup get(int id);


    public UserGroup getByName(String name);


    public List<UserGroup> listAll();


    public List<UserGroup> listByUser(int userId);


    public void update(UserGroup obj);
}