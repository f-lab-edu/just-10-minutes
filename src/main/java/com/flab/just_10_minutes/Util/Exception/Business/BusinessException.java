package com.flab.just_10_minutes.Util.Exception.Business;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private String message;

    //message constant
    public static final String DUPLICATED_REGISTER = "Duplicate User Registration Request";
    public static final String INIT_POINT_COULD_NOT_MINUS = "Initial PointHistory Couldn't Minus";

    //constant
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
