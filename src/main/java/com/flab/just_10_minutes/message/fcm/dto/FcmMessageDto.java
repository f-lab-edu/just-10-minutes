package com.flab.just_10_minutes.message.fcm.dto;

import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class FcmMessageDto {

    private String messageId;
    private String token;
    private Long campaignId;
    private Boolean isSend;

    public static FcmMessageDto from(FcmMessage fcmMessage) {
        return FcmMessageDto.builder()
                            .messageId(fcmMessage.getMessageId())
                            .token(fcmMessage.getToken())
                            .campaignId(fcmMessage.getCampaignId())
                            .isSend(fcmMessage.getIsSend())
                            .build();
    }
}
