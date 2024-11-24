package com.flab.just_10_minutes.notification.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@With
public class FcmNotification {

    private String notificationId;
    private String receiverId;
    private String destination;
    private Long campaignId;
    private Boolean isSend;

    public static FcmNotification from(FcmNotificationEvent event, FcmToken fcmToken) {
        return FcmNotification.builder()
                            .notificationId(event.getEventId())
                            .receiverId(event.getReceiverId())
                            .destination(fcmToken.getToken())
                            .campaignId(event.getCampaignId())
                            .build();
    }

    public FcmNotification update(final Boolean isSend) {
        return this.withIsSend(isSend);
    }
}
