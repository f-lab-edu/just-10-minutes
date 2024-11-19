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
public class Notification {

    private String eventId;
    private Long campaignId;
    private String destination;
    private ChannelType channelType;
    private Boolean isSend;

    public static Notification from(
                                    final String eventId,
                                    final Long campaignId,
                                    final String destination,
                                    final ChannelType channelType,
                                    final Boolean isSend) {
        return Notification.builder()
                        .eventId(eventId)
                        .campaignId(campaignId)
                        .destination(destination)
                        .channelType(channelType)
                        .isSend(isSend)
                        .build();
    }

    public Notification update(final Boolean isSend) {
        return this.withIsSend(isSend);
    }
}
