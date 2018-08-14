package org.memehazard.wheel.core.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEmailAddressValidator implements ConstraintValidator<ValidEmailAddress, String>
{
    
    // This expression is more strict than defined in the RFC, as it disallows some special characters that can be included using 
    // escape sequences. This is because some vendors also flout the standards and disallow certain addresses. Thus, we only allow users
    // to use email addresses that fit work with pretty much all vendors.
    private static final String EMAIL_PATTERN =
                                                "^[_A-Za-z0-9-+.=#$%&{}|~]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private Pattern             pattern       = null;


    /**
     * Initialize the validator given the annotation parameters provided
     */
    @Override
    public void initialize(ValidEmailAddress constraintAnnotation)
    {
        if (pattern == null)
            pattern = Pattern.compile(EMAIL_PATTERN);
    }


    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context)
    {
        return pattern.matcher(obj).matches();
    }
}
