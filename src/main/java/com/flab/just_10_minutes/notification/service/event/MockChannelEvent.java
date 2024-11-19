package com.flab.just_10_minutes.notification.service.event;

import com.flab.just_10_minutes.notification.domain.ChannelType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MockChannelEvent implements ChannelEvent {

    private String eventId;
    private ChannelType channelType;
}
