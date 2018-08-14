package org.memehazard.wheel.textCompletion.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.textCompletion.test.TextCompletionTestDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class TextCompletionDAOTest
{

    @SuppressWarnings("unused")
    private Logger               log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TextCompletionDAO     dao;

    @Autowired
    public TextCompletionTestDAO dao_test;


    @Before
    public void before()
    {
        dao_test.createTestData();
    }


    @Test
    @Transactional
    public void test_listCompletionsWithPrefix()
    {
        // TEST - Is number of strings returned constrained by limit?
        List<String> results = dao.listCompletionsWithPrefix("value", 2);
        Assert.assertEquals(2, results.size());

        // TEST - Are strings returned in correct order?
        // TEST - Are non-matches correctly not returned?
        results = dao.listCompletionsWithPrefix("value", 4);
        Assert.assertEquals(3, results.size());
        Assert.assertEquals("value_1", results.get(0));
        Assert.assertEquals("value_2", results.get(1));
        Assert.assertEquals("value_3", results.get(2));
    }
    

    @Test
    @Transactional
    public void test_listCompletions()
    {
        // TEST - Is number of strings returned constrained by limit?
        List<String> results = dao.listCompletionsWithPrefix("value", 2);
        Assert.assertEquals(2, results.size());

        // TEST - Are strings returned in correct order?
        // TEST - Are non-matches correctly not returned?
        results = dao.listCompletions("value", 4);
        Assert.assertEquals(4, results.size());
        Assert.assertEquals("value_1", results.get(0));
        Assert.assertEquals("value_2", results.get(1));
        Assert.assertEquals("value_3", results.get(2));
        Assert.assertEquals("zvalue_4", results.get(3));
    }
    
    
}
