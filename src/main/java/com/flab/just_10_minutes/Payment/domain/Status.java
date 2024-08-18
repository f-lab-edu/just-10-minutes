package com.flab.just_10_minutes.Payment.domain;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Status {
    PAID("paid"),
    CANCELLED("cancelled"),
    FAIL("fail");

    private String lable;

    Status(String lable) {
        this.lable = lable;
    }

    private static Map<String, Status> lableStatusMap =
            Stream.of(values()).collect(Collectors.toMap(Status::getLable, e -> e));

    public static Status from(String lable) {
        Status status = lableStatusMap.get(lable);
        if (status == null) throw new RuntimeException("Not Found");
        return status;
    }
}
