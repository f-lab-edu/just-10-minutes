package com.flab.just_10_minutes.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@With
public class NotificationEvent {

    private String eventId;
    private String receiverId;
    private Long campaignId;
    private Boolean isPublished;

    public static NotificationEvent from(final String eventId, final String receiverId, final Long campaignId) {
        return NotificationEvent.builder()
                                .eventId(eventId)
                                .receiverId(receiverId)
                                .campaignId(campaignId)
                                .isPublished(false)
                                .build();
    }
    
    public NotificationEvent update(final Boolean isPublished) {
        return this.withIsPublished(isPublished);
    }
}
