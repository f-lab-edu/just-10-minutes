package com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flab.just_10_minutes.common.exception.infra.fcm.FcmCredentialException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcmApiV1FailResponse {

    private Error error;

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Error {
        private String status;
        private String message;
        private int code;
    }

    public static FcmApiV1FailResponse fromInvalidCredential(FcmCredentialException e) {
        return FcmApiV1FailResponse.builder()
                .error(Error.builder()
                        .status(e.getHttpStatus().toString())
                        .message(e.getMessage())
                        .code(e.getHttpStatus().value())
                        .build())
                .build();
    }

    public static FcmApiV1FailResponse fromReadTimeout() {
        return FcmApiV1FailResponse.builder()
                                .error(Error.builder()
                                            .status(HttpStatus.GATEWAY_TIMEOUT.toString())
                                            .message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase())
                                            .code(HttpStatus.GATEWAY_TIMEOUT.value())
                                            .build())
                                .build();
    }
}
