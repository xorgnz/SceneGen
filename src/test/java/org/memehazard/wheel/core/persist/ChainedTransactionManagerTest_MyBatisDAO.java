package org.memehazard.wheel.core.persist;

import org.memehazard.wheel.core.persist.ChainedTransactionManagerTest.TestObject;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface ChainedTransactionManagerTest_MyBatisDAO extends MyBatisDAO
{
    public void add(TestObject name);


    public TestObject get(int id);
}
