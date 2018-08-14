package org.memehazard.wheel.activity.dao;

import java.util.List;

import org.memehazard.wheel.activity.model.ActivityInstance;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface ActivityInstanceDAO extends MyBatisDAO
{
    public void add(ActivityInstance obj);


    public void delete(int id);


    public ActivityInstance get(int id);


    public List<ActivityInstance> listAll();


    public List<ActivityInstance> listByTemplate(int templateId);


    public void update(ActivityInstance obj);
}
