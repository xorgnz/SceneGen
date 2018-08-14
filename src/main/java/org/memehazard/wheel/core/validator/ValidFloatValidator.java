package org.memehazard.wheel.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFloatValidator implements ConstraintValidator<ValidFloat, String>
{
    private float min;
    private float max;


    @Override
    public void initialize(ValidFloat constraintAnnotation)
    {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();

        if (Float.isNaN(min))
            min = Float.NEGATIVE_INFINITY;

        if (Float.isNaN(max))
            max = Float.POSITIVE_INFINITY;
    }


    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context)
    {
        try
        {
            if (obj.trim().equals(""))
                return true;
            
            float f = Float.parseFloat(obj);

            if (f < min)
                return false;

            if (f > max)
                return false;

            if (Float.isNaN(f))
                return false;
            
            return true;
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
    }
}
