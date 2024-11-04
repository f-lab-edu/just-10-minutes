package com.flab.just_10_minutes.message.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FcmMessage {

    private String messageId;
    private String token;
    private Long campaignId;
    private Boolean isSend;

    public static FcmMessage create(final String messageId, FcmToken fcmToken, FcmCampaign fcmCampaign) {
        return FcmMessage.builder()
                        .messageId(messageId)
                        .token(fcmToken.getToken())
                        .campaignId(fcmCampaign.getId())
                        .isSend(false)
                        .build();
    }

    public static FcmMessage update(final String messageId, final Boolean isSend) {
        return FcmMessage.builder()
                .messageId(messageId)
                .isSend(isSend)
                .build();
    }
}
