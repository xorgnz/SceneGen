package org.memehazard.wheel.activity.test;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface TestDAO_ActivityManager extends MyBatisDAO
{
    public Object countActivityInstances();


    public Object countActivityTemplateParameters();


    public Object countActivityTemplates();
}
