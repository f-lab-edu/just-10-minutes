package com.flab.just_10_minutes.common.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumCheckValidator.class)
public @interface PhoneNum {

    String message() default "잘못된 휴대폰 번호입니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
