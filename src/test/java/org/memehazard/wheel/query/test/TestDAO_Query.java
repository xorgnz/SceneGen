package org.memehazard.wheel.query.test;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface TestDAO_Query extends MyBatisDAO
{
    public int clear();
}