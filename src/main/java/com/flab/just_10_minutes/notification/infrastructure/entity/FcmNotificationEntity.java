package com.flab.just_10_minutes.notification.infrastructure.entity;

import com.flab.just_10_minutes.notification.domain.FcmNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@With
public class FcmNotificationEntity {

    private Long id;
    private String NotificationId;
    private Long campaignId;
    private String destination;
    private Boolean isSend;

    public static FcmNotificationEntity from(FcmNotification fcmNotification) {
        return FcmNotificationEntity.builder()
                                 .NotificationId(fcmNotification.getNotificationId())
                                .campaignId(fcmNotification.getCampaignId())
                                .destination(fcmNotification.getDestination())
                                .isSend(fcmNotification.getIsSend())
                                .build();
    }

    public static FcmNotification toDomain(FcmNotificationEntity notificationEntity) {
        return FcmNotification.builder()
                        .notificationId(notificationEntity.getNotificationId())
                        .campaignId(notificationEntity.getCampaignId())
                        .destination(notificationEntity.getDestination())
                        .isSend(notificationEntity.getIsSend())
                        .build();
    }
}
