package org.memehazard.wheel.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniquePermissionNameValidator implements ConstraintValidator<UniquePermissionName, Object>
{
    private static RbacFacade   _facade;
    private static final String FIELDNAME = "name";


    public UniquePermissionNameValidator()
    {
    }


    @Autowired
    public UniquePermissionNameValidator(RbacFacade facade)
    {
        _facade = facade;
    }


    @Override
    public void initialize(UniquePermissionName constraintAnnotation)
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
            Permission p = _facade.getPermissionByName(s);

            // Valid if no matching permission is found or if matching permission is self
            return p == null || p.equals(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
