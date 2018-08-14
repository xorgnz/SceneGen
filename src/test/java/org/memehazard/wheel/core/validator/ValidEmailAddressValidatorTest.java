package org.memehazard.wheel.core.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class ValidEmailAddressValidatorTest
{

    /**
     * Validator is more restrictive than standards, as standards allow addresses that are disallowed by some
     * vendors
     */
    @Test
    public void test()
    {
        ValidEmailAddressValidator v = new ValidEmailAddressValidator();
        v.initialize(null);

        // Valid examples
        Assert.assertTrue(v.isValid("Test@Test.com", null));// Basic example
        Assert.assertTrue(v.isValid("+#%$&={}|~@test.com", null)); // Nonstandard characters
        Assert.assertTrue(v.isValid("-Test.foo+foo@test.test-test.com", null)); // Complicated domain

        // Invalid examples
        Assert.assertFalse(v.isValid("Testtest.com", null)); // Missing @
        Assert.assertFalse(v.isValid("Te@st@test.com", null)); // Too many @s
        Assert.assertFalse(v.isValid("foo@test.c", null)); // TLD too short
        Assert.assertFalse(v.isValid("foo@test.c-c", null)); // TLD contains invalid char
        Assert.assertFalse(v.isValid("foo@test.c+c", null)); // TLD contains invalid char
        Assert.assertFalse(v.isValid("foo@test.c=c", null)); // TLD contains invalid char
        Assert.assertFalse(v.isValid("foo@test.c.c", null)); // TLD contains invalid char
        Assert.assertFalse(v.isValid("foo@!test.com", null)); // Domain contains invalid char
        Assert.assertFalse(v.isValid("foo@?test.com", null)); // Domain contains invalid char
        Assert.assertFalse(v.isValid("foo@\"test.com", null)); // Domain contains invalid char
        Assert.assertFalse(v.isValid("foo@+test.com", null)); // Domain contains invalid char
        Assert.assertFalse(v.isValid("foo@=test.com", null)); // Domain contains invalid char
        Assert.assertFalse(v.isValid("\"foo@test.com", null)); // Local contains invalid char
        Assert.assertFalse(v.isValid("]foo@test.com", null)); // Local contains invalid char
        Assert.assertFalse(v.isValid("[foo@test.com", null)); // Local contains invalid char
        Assert.assertFalse(v.isValid(")foo@test.com", null)); // Local contains invalid char
        Assert.assertFalse(v.isValid("(foo@test.com", null)); // Local contains invalid char
    }
}
