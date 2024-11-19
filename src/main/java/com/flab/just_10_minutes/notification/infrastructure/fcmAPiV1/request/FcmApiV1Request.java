package com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.Notification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FcmApiV1Request {

    private boolean validateOnly;
    private Message message;

    @Data
    @Builder
    public static class Message {
        private FcmNotification notification;
        private String token;
    }

    @Data
    @Builder
    public static class FcmNotification {
        private String title;
        private String body;
        private String image;
    }

    public static FcmApiV1Request from(Notification notification, Campaign fcmCampaign) {
        return FcmApiV1Request.builder()
                .message(Message.builder()
                        .token(notification.getDestination())
                        .notification(FcmNotification.builder()
                                                .title(fcmCampaign.getTitle())
                                                .body(fcmCampaign.getBody())
                                                .image(null)
                                                .build())
                        .build())
                .validateOnly(false)
                .build();
    }
}
