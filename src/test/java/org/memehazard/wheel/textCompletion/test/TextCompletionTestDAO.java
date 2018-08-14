package org.memehazard.wheel.textCompletion.test;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface TextCompletionTestDAO extends MyBatisDAO
{
    public void createTestData();
}
