package org.memehazard.wheel.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUploadedValidator implements ConstraintValidator<MultipartFileUploaded, MultipartFile>
{
    @Override
    public void initialize(MultipartFileUploaded constraintAnnotation)
    {
        // Do nothing - no initialization required
    }


    @Override
    public boolean isValid(MultipartFile obj, ConstraintValidatorContext context)
    {
        return !obj.isEmpty();
    }
}
