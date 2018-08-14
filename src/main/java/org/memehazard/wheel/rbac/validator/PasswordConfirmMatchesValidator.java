package org.memehazard.wheel.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;


public class PasswordConfirmMatchesValidator implements ConstraintValidator<PasswordConfirmMatches, Object>
{
    @Override
    public void initialize(PasswordConfirmMatches constraintAnnotation)
    {
        // Do nothing - no initialization required
    }


    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context)
    {
        try
        {
            String password = BeanUtils.getProperty(o, "password");
            String passwordConfirm = BeanUtils.getProperty(o, "passwordConfirm");
            return password.equals(passwordConfirm);
        }
        catch (final Exception e)
        {
            return false;
        }
    }
}
