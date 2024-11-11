package com.flab.just_10_minutes.message.fcm.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@With
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
}
