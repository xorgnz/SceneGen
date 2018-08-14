package org.memehazard.wheel.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String>
{
    @Override
    public void initialize(StrongPassword constraintAnnotation)
    {
        // Do nothing - no initialization required
    }


    @Override
    public boolean isValid(String s, ConstraintValidatorContext context)
    {
        // Don't validate an empty password
        if (s.length() == 0)
            return true;
        
        if (s.length() < 8)
            return false;
        
        if (!StringUtils.containsAny(s, "0123456789"))
            return false;

        if (!StringUtils.containsAny(s, "abcdefghijklmnopqrstuvwxyz"))
            return false;

        if (!StringUtils.containsAny(s, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
            return false;

        return true;
    }
}
