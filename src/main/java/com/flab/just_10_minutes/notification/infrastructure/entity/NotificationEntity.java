package com.flab.just_10_minutes.notification.infrastructure.entity;

import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.domain.Notification;
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
public class NotificationEntity {

    private Long id;
    private String eventId;
    private Long campaignId;
    private String destination;
    private ChannelType channelType;
    private Boolean isSend;

    public static NotificationEntity from(Notification notification) {
        return NotificationEntity.builder()
                                 .eventId(notification.getEventId())
                                .campaignId(notification.getCampaignId())
                                .destination(notification.getDestination())
                                .channelType(notification.getChannelType())
                                .isSend(notification.getIsSend())
                                .build();
    }

    public static Notification toDomain(NotificationEntity notificationEntity) {
        return Notification.builder()
                        .eventId(notificationEntity.getEventId())
                        .campaignId(notificationEntity.getCampaignId())
                        .destination(notificationEntity.getDestination())
                        .channelType(notificationEntity.getChannelType())
                        .isSend(notificationEntity.getIsSend())
                        .build();
    }
}
