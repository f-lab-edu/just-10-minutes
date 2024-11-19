package com.flab.just_10_minutes.notification.infrastructure.entity;

import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationEventEntity {

    private Long id;
    private String eventId;
    private String receiverId;
    private Long campaignId;
    private Boolean isPublished;

    public static NotificationEventEntity from(NotificationEvent event) {
        return NotificationEventEntity.builder()
                                    .eventId(event.getEventId())
                                    .receiverId(event.getReceiverId())
                                    .campaignId(event.getCampaignId())
                                    .isPublished(event.getIsPublished())
                                    .build();
    }

    public static NotificationEvent toDomain(NotificationEventEntity eventEntity) {
        return NotificationEvent.builder()
                                .eventId(eventEntity.getEventId())
                                .receiverId(eventEntity.getReceiverId())
                                .campaignId(eventEntity.getCampaignId())
                                .isPublished(eventEntity.getIsPublished())
                                .build();
    }
}
