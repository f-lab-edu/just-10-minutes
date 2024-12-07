package com.flab.just_10_minutes.common.exception.infra.iamport;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IamportException extends RuntimeException {

    private static final String message = "Payment Error Occurred";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public IamportException(String cause) {
        super(message, new Throwable(cause));
    }
}
