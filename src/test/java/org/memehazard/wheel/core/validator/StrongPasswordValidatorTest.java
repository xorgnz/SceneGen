package org.memehazard.wheel.core.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class StrongPasswordValidatorTest
{
    /**
     * Validator is more restrictive than standards, as standards allow addresses that are disallowed by some
     * vendors
     */
    @Test
    public void test()
    {
        StrongPasswordValidator v = new StrongPasswordValidator();
        v.initialize(null);

        // Invalid examples
        Assert.assertFalse(v.isValid("Ab1", null)); // Too short
        Assert.assertFalse(v.isValid("Ab34567", null)); // Still too short
        Assert.assertFalse(v.isValid("A2345678901234567890", null)); // No lower case
        Assert.assertFalse(v.isValid("a2345678901234567890", null)); // No upper case
        Assert.assertFalse(v.isValid("abcdefghijkl", null)); // No numbers

        // Valid examples
        Assert.assertTrue(v.isValid("", null)); // We don't validate a blank password
        Assert.assertTrue(v.isValid("Ab345678", null)); // Minimum length
        Assert.assertTrue(v.isValid("Ab3456789012345678901234567890", null)); // Quite length
    }
}
