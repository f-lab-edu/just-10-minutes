package com.flab.just_10_minutes.common.exception.infra.fcm;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FcmCredentialException extends RuntimeException {

    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public FcmCredentialException(String cause) {
        super(cause);
    }
}
