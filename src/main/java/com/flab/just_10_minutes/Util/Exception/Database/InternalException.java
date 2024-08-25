package com.flab.just_10_minutes.Util.Exception.Database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalException extends DatabaseException {

    public static final String FAIL_TO_INSERT = "Fail To Insert Error";
    public static final String FAIL_TO_UPDATE = "Fail To Update Error";
    public static final String FAIL_TO_DELETE = "Fail To Delete Error";

    public InternalException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
