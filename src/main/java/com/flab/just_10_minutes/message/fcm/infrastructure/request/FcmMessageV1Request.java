package com.flab.just_10_minutes.message.fcm.infrastructure.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FcmMessageV1Request {

    private boolean validateOnly;
    private Message message;

    @Data
    @Builder
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Data
    @Builder
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    public static FcmMessageV1Request from(FcmMessage fcmMessage, FcmCampaign fcmCampaign) {
        return FcmMessageV1Request.builder()
                .message(Message.builder()
                        .token(fcmMessage.getToken())
                        .notification(Notification.builder()
                                                .title(fcmCampaign.getTitle())
                                                .body(fcmCampaign.getBody())
                                                .image(null)
                                                .build())
                        .build())
                .validateOnly(false)
                .build();
    }
}
