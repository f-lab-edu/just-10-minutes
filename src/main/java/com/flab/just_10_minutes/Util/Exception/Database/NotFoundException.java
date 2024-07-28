package com.flab.just_10_minutes.Util.Exception.Database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends DatabaseException{

    public static final String NOT_EXIST_USER= "Not Exist User";

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
