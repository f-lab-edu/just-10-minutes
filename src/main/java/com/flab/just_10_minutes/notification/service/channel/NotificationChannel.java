package com.flab.just_10_minutes.notification.service.channel;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import com.flab.just_10_minutes.notification.service.event.ChannelEvent;
import com.flab.just_10_minutes.user.domain.User;

public interface NotificationChannel {

    Boolean isApplicable(User user);
    void createNotification(User user, NotificationEvent event, Campaign campaign);
    ChannelType getChannelType();
    ChannelEvent createChannelEvent(String eventId);
}
