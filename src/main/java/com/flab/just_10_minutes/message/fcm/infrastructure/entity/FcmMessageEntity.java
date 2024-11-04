package com.flab.just_10_minutes.message.fcm.infrastructure.entity;

import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmMessageEntity {

    private Long id;
    private String messageId;
    private String token;
    private Long campaignId;
    private Boolean isSend;

    public static FcmMessageEntity from(FcmMessage fcmMessage) {
        return FcmMessageEntity.builder()
                    .messageId(fcmMessage.getMessageId())
                    .token(fcmMessage.getToken())
                    .campaignId(fcmMessage.getCampaignId())
                    .isSend(fcmMessage.getIsSend())
                    .build();
    }

    public static FcmMessage toDomain(FcmMessageEntity fcmMessageEntity) {
        return FcmMessage.builder()
                        .token(fcmMessageEntity.getToken())
                        .campaignId(fcmMessageEntity.getCampaignId())
                        .isSend(fcmMessageEntity.getIsSend())
                        .build();
    }
}
