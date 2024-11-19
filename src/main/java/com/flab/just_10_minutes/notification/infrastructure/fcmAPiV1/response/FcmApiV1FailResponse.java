package com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcmApiV1FailResponse {

    private Error error;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Error {
        private String status;
        private String message;
        private int code;
    }
}
