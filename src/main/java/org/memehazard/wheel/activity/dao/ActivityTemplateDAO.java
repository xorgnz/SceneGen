package org.memehazard.wheel.activity.dao;

import java.util.List;

import org.memehazard.wheel.activity.model.ActivityTemplate;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface ActivityTemplateDAO extends MyBatisDAO
{

    public void add(ActivityTemplate obj);


    public void addParameters(ActivityTemplate template);


    public void delete(int id);


    public void deleteParameters(int id);


    public ActivityTemplate get(int id);


    public List<ActivityTemplate> listAll();


    public void update(ActivityTemplate obj);


    public void updateParameters(ActivityTemplate obj);

}
