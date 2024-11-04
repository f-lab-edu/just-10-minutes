package com.flab.just_10_minutes.message.fcm.infrastructure.entity;

import com.flab.just_10_minutes.message.fcm.domain.FcmToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmTokenEntity {

    private Long id;
    private String loginId;
    private String token;

    public static FcmTokenEntity from(FcmToken fcmToken) {
        return FcmTokenEntity.builder()
                    .loginId(fcmToken.getLoginId())
                    .token(fcmToken.getToken())
                    .build();
    }

    public static FcmToken toDomain(FcmTokenEntity fcmTokenEntity) {
        return FcmToken.builder()
                        .loginId(fcmTokenEntity.getLoginId())
                        .token(fcmTokenEntity.getToken())
                        .build();
    }
}
