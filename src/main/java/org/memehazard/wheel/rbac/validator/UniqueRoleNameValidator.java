package org.memehazard.wheel.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, Object>
{
    private static RbacFacade   _facade;
    private static final String FIELDNAME = "name";


    public UniqueRoleNameValidator()
    {
    }


    @Autowired
    public UniqueRoleNameValidator(RbacFacade facade)
    {
        _facade = facade;
    }


    @Override
    public void initialize(UniqueRoleName constraintAnnotation)
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
            Role r = _facade.getRoleByName(s);

            // Valid if no matching role is found or if matching role is self
            return r == null || r.equals(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
