package com.flab.just_10_minutes.Util.ErrorResult;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorResult {

    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
