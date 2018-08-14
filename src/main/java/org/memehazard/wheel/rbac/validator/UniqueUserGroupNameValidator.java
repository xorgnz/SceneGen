package org.memehazard.wheel.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniqueUserGroupNameValidator implements ConstraintValidator<UniqueUserGroupName, Object>
{
    private static RbacFacade   _facade;
    private static final String FIELDNAME = "name";


    public UniqueUserGroupNameValidator()
    {
    }


    @Autowired
    public UniqueUserGroupNameValidator(RbacFacade facade)
    {
        _facade = facade;
    }


    @Override
    public void initialize(UniqueUserGroupName constraintAnnotation)
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
            UserGroup ug = _facade.getUserGroupByName(s);

            // Valid if no matching user group is found or if matching user group is self
            return ug == null || ug.equals(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
