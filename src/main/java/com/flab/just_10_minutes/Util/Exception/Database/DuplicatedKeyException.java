package com.flab.just_10_minutes.Util.Exception.Database;

public class DuplicatedKeyException extends DatabaseException{

    public DuplicatedKeyException(String message) {
        super(message);
    }
}
