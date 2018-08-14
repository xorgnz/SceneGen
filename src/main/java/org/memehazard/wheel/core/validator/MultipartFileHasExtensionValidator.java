package org.memehazard.wheel.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileHasExtensionValidator implements
        ConstraintValidator<MultipartFileHasExtension, MultipartFile>
{
    private String[] extensions;


    @Override
    public void initialize(MultipartFileHasExtension constraintAnnotation)
    {
        extensions = org.apache.commons.lang.StringUtils.split(constraintAnnotation.extensions(), ",");
    }


    @Override
    public boolean isValid(MultipartFile obj, ConstraintValidatorContext context)
    {
        // A non-existent file is considered to be valid for all file extensions
        // This prevents this rule being triggered when the file is optional, and keeps things atomic
        if (obj.isEmpty())
            return true;

        String ext = FilenameUtils.getExtension(obj.getOriginalFilename());
        for (String s : extensions)
        {
            if (s.trim().equalsIgnoreCase(ext))
                return true;
        }

        return false;
    }
}
