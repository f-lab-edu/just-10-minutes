package com.flab.just_10_minutes.util.exception.database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DatabaseException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public DatabaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
