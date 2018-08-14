package org.memehazard.wheel.core.persist;

import java.io.Serializable;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.neo4j.graphdb.NotInTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for effect of chained transaction management on the two data sources we're using.
 * 
 * @author xorgnz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class ChainedTransactionManagerTest
{
    private Logger                                   log        = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChainedTransactionManagerTest_MyBatisDAO dao_myBatisDao;

    @Autowired
    private Neo4jTemplate                            template;

    private int                                      id_mybatis = 0;


    @Before
    public void before()
    {
        log.trace(".before()");
    }


    @AfterTransaction
    public void afterTransaction()
    {
        Assert.assertNull("MyBatis not in transaction - rollback did not clear DB", dao_myBatisDao.get(id_mybatis));
    }


    /**
     * Tests whether mybatis operations are captured within transactions managed by the ChainedTransactionManager
     * - An object is created in postgreSQL using MyBatis
     * - An object is created in Neo4J using Neo4J
     * - A rollback is triggered using a broken neo4j call is made, triggering a rollback.
     * 
     * If Neo4J is not properly captured in the transaction, an exception will be thrown when a write action is
     * attempted.
     * If MyBatis is not properly captured in the transaction, objects will remain after the transaction is rolled back.
     */
    @Test
    @Transactional
    public void testMyBatisIsInTransaction()
    {
        // Act - Persist an object using mybatis
        TestObject t = new TestObject("test name");
        dao_myBatisDao.add(t);
        id_mybatis = t.getId();
        Assert.assertEquals("test name", dao_myBatisDao.get(t.getId()).getName());
    }


    /**
     * Tests whether Neo4J operations are captured within transactions managed by the ChainedTransactionManager
     * - An object is created in Neo4J using Neo4J. If
     * 
     * If Neo4J is not properly captured in the transaction, an exception will be thrown when a write action is
     * attempted.
     */
    @Test
    @Transactional
    public void testNeo4JIsInTransaction()
    {
        // Act - Persist an object using Neo4J
        // On fail, an exception is thrown
        try
        {
            template.save(new Curriculum("name", "creator", "description"));
        }
        catch (NotInTransactionException e)
        {
            Assert.fail("Neo4J not in transaction - NotInTransactionException thrown");
            e.printStackTrace();
        }
    }


    public static class TestObject implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private int               id;
        private String            name;


        public TestObject()
        {

        }


        public TestObject(String name)
        {
            this.name = name;
        }


        public int getId()
        {
            return id;
        }


        public void setId(int id)
        {
            this.id = id;
        }


        public String getName()
        {
            return name;
        }


        public void setName(String name)
        {
            this.name = name;
        }
    }
}
