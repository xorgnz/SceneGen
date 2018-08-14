package org.memehazard.wheel.rbac.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.test.RbacTestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class UniqueUsernameValidatorTest
{

    @Autowired
    private RbacFacade facade;
    @Autowired
    public RbacTestDAO dao_test;


    @Before
    public void before()
    {
        dao_test.createTestData();
    }


    /**
     * Validator is more restrictive than standards, as standards allow addresses that are disallowed by some
     * vendors
     */
    @Test
    @Transactional
    public void test()
    {
        UniqueUsernameValidator v = new UniqueUsernameValidator();

        // Invalid examples
        Assert.assertFalse(v.isValid(new TestObject("username0"), null)); // Exists

        // Valid examples
        Assert.assertTrue(v.isValid(new TestObject("Ab345678"), null)); // Minimum length
        Assert.assertTrue(v.isValid(facade.getUserByUsername("username0"), null)); // Exists, but only on given obj
    }


    public class TestObject
    {

        private String username;


        public TestObject(String username)
        {
            this.username = username;
        }


        public String getUsername()
        {
            return username;
        }
    }
}
