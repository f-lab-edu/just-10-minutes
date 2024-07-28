package com.flab.just_10_minutes.Util.Exception.Database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalException extends DatabaseException{

    public static final String FAIL_TO_INSERT = "Fail to Insert";

    public InternalException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
