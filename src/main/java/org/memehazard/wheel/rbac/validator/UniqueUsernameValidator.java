package org.memehazard.wheel.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, Object>
{
    private static RbacFacade   _facade;
    private static final String FIELDNAME = "username";


    public UniqueUsernameValidator()
    {
    }


    @Autowired
    public UniqueUsernameValidator(RbacFacade facade)
    {
        _facade = facade;
    }


    @Override
    public void initialize(UniqueUsername constraintAnnotation)
    {
        // Do nothing - no initialization required
    }


    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context)
    {
        try
        {
            String s = BeanUtils.getProperty(o, FIELDNAME);

            // Search for a user with this username
            User u = _facade.getUserByUsername(s);

            // Valid if no matching user is found or if matching user is self
            return u == null || u.equals(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
