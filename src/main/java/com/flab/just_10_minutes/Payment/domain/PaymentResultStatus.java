package com.flab.just_10_minutes.Payment.domain;

import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.NOT_FOUND;

@Getter
public enum PaymentResultStatus {
    PAID("paid"),
    CANCELLED("cancelled"),
    FAIL("fail");

    private String lable;

    PaymentResultStatus(String lable) {
        this.lable = lable;
    }

    private static Map<String, PaymentResultStatus> statusLableMap =
            Stream.of(values()).collect(Collectors.toMap(PaymentResultStatus::getLable, e -> e));

    public static PaymentResultStatus from(String lable) {
        PaymentResultStatus status = statusLableMap.get(lable);
        if (status == null) throw new NotFoundException(NOT_FOUND, "Payment Result");
        return status;
    }
}
