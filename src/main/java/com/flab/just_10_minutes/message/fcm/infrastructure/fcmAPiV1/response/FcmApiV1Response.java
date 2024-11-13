package com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class FcmApiV1Response {

    private int code;
    private String status;
    private String message;

    public static FcmApiV1Response withSuccess(FcmApiV1SuccessResponse response) {
        return FcmApiV1Response.builder()
                            .code(200)
                            .status("OK")
                            .message(response.getName())
                            .build();
    }

    public static FcmApiV1Response withFailure(FcmApiV1FailResponse response) {
        return FcmApiV1Response.builder()
                            .code(response.getError().getCode())
                            .status(response.getError().getStatus())
                            .message(response.getError().getMessage())
                            .build();
    }
}
