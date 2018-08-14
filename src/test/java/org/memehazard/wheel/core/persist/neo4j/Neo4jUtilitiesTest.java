package org.memehazard.wheel.core.persist.neo4j;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class Neo4jUtilitiesTest
{
    @Autowired
    private Neo4jUtilities utils;

    private Logger         log = LoggerFactory.getLogger(this.getClass());


    /**
     * After any test, ensure all transactions are closed
     */
    @After
    public void after()
    {
        log.trace("------------------- AFTER");

        // ACT - Roll back any current transaction
        utils.finishRunningTransaction();
    }


    /**
     * Before any test, clear the database
     */
    @Before
    public void before()
    {
        log.trace("------------------- BEFORE");

        // ACT - Clear DB
        utils.clearDatabase();
    }


    /**
     * Test Neo4jUtilities.clearDatabase() operation
     * 
     * Results
     * - Outside the transaction
     * - - DB clearance is countable
     * - - DB clearance is query-able
     */
    @Test
    public void testClearDatabase()
    {
        log.trace("Start testClearDatabase");

        // ACT - Prepare test objects
        Transaction tx = utils.beginTx();
        utils.getTemplate().save(new SimpleTestNode());
        tx.success();
        tx.finish();


        // ACT - Clear DB
        utils.clearDatabase();

        // TEST - DB clearance is apparent to .count()
        Assert.assertEquals(0, utils.getTemplate().count(SimpleTestNode.class));

        // TEST - DB clearance is apparent to .query()
        Assert.assertEquals(0, Iterables.toList(utils.getTemplate().query("START n=node(*) RETURN n;", null)).size());

        log.trace("Finish testClearDatabase");
    }


    /**
     * Test Neo4jUtilities.countAllNodes
     */
    @Test
    public void testCountAllNodes()
    {
        log.trace("Start testCountAllNodes");

        // ACT - Prepare test objects
        Transaction tx = utils.beginTx();
        utils.getTemplate().save(new SimpleTestNode());
        utils.getTemplate().save(new SimpleTestNode());
        tx.success();
        tx.finish();

        // TEST - Is correct count yielded?
        Assert.assertEquals(2, utils.countAllNodes());

        log.trace("Finish testCountAllNodes");
    }


    /**
     * test closure of a running transaction
     */
    @Test
    public void testTransactionStartStop()
    {
        log.trace("Start testFinishRunningTransaction");

        // ACT - Start transaction
        utils.beginTx();

        // TEST - Transaction is running
        Assert.assertTrue(utils.getTemplate().transactionIsRunning());

        // ACT - Finish transaction
        utils.finishRunningTransaction();

        // TEST - Transaction is closed
        Assert.assertFalse(utils.getTemplate().transactionIsRunning());

        log.trace("finish testFinishRunningTransaction");
    }
}
