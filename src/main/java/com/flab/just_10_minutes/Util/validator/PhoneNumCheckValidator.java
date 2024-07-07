package com.flab.just_10_minutes.Util.validator;

import ch.qos.logback.core.util.StringUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumCheckValidator implements ConstraintValidator<PhoneNumCheck, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtil.isNullOrEmpty(value)) {
            return false;
        }

        return value.matches("^010-\\d{3,4}-\\d{4}$");

    }
}
