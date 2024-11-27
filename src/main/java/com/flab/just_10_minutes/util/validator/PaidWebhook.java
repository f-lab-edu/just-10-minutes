package com.flab.just_10_minutes.util.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaidWebhookValidator.class)
public @interface PaidWebhook {

    String message() default "웹훅 status가 paid가 아닙니다";

    Class[] groups() default {};

    Class[] payload() default {};
}
