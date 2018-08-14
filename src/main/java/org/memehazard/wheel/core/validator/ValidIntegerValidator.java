package org.memehazard.wheel.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidIntegerValidator implements ConstraintValidator<ValidInteger, String>
{
    @Override
    public void initialize(ValidInteger constraintAnnotation)
    {
        // Do nothing - no initialization required
    }


    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context)
    {
        try
        {
            Integer.parseInt(obj);
            return true;
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
    }
}
