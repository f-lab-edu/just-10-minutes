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
public class FcmNotificationEvent {

    private String eventId;
    private String receiverId;
    private Long campaignId;

    public static FcmNotificationEvent from(final String eventId, final String receiverId, final Long campaignId) {
        return FcmNotificationEvent.builder()
                .eventId(eventId)
                .receiverId(receiverId)
                .campaignId(campaignId)
                .build();
    }
}
