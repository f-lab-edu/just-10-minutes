package com.flab.just_10_minutes.Util.ErrorResult;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DaoErrorResult {

    INSERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to Insert."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
