package org.memehazard.wheel.rbac.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class UserTest
{

    @Test
    public void testEquals()
    {
        User u0 = new User();
        u0.setId(23);

        User u1 = new User();
        u1.setId(23);

        Assert.assertTrue(u0.equals(u1));
    }
}
