package com.flab.just_10_minutes.Util.Exception.Iamport;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IamportException extends RuntimeException {

    private static final String message = "Network Error";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public IamportException( Throwable cause) {
        super(message, cause);
    }
}
