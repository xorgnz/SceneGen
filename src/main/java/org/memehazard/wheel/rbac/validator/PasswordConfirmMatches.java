package org.memehazard.wheel.rbac.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = PasswordConfirmMatchesValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface PasswordConfirmMatches
{

    public abstract Class<?>[] groups() default {};


    public abstract String message() default "{org.memehazard.wheel.core.validator.PasswordConfirmMatches}";


    public abstract Class<? extends Payload>[] payload() default {};

}