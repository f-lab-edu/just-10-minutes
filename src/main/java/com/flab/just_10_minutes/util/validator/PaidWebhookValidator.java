package com.flab.just_10_minutes.util.validator;

import com.flab.just_10_minutes.payment.domain.PaymentResultStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaidWebhookValidator implements ConstraintValidator<PaidWebhook, PaymentResultStatus> {


    @Override
    public boolean isValid(PaymentResultStatus value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }

        return value == PaymentResultStatus.PAID;
    }
}
