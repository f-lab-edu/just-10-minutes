package com.flab.just_10_minutes.Util.Exception.Database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicatedKeyException extends DatabaseException {

    public static final String DUPLICATED_KEY = "Duplicated Key Error";

    public DuplicatedKeyException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
