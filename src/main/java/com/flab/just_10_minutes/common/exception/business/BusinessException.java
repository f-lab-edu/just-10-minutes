package com.flab.just_10_minutes.common.exception.business;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private String message;

    //constant
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
