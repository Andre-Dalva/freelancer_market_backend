package com.andretk.backend1.validation;

import com.andretk.backend1.enums.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidRoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        try {
            UserRole.valueOf(value.toUpperCase());
            return true;
        }
        catch (IllegalArgumentException e){
            return false;
        }
    }
}
