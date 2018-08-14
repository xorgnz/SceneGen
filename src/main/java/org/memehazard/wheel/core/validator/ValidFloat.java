package org.memehazard.wheel.core.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = ValidFloatValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface ValidFloat
{
    float min() default Float.NEGATIVE_INFINITY;


    float max() default Float.POSITIVE_INFINITY;


    public abstract Class<?>[] groups() default {};


    public abstract String message() default "{org.memehazard.wheel.core.validator.ValidFloat}";


    public abstract Class<? extends Payload>[] payload() default {};

}