package com.flab.just_10_minutes.message.fcm.domain;

import com.flab.just_10_minutes.message.fcm.dto.FcmTokenRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class FcmToken {

    private String loginId;
    private String token;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static FcmToken from(FcmTokenRequest fcmTokenRequest) {
        return FcmToken.builder()
                    .loginId(fcmTokenRequest.getLoginId())
                    .token(fcmTokenRequest.getToken())
                    .build();
    }
}
