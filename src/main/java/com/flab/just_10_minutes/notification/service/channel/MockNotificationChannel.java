package com.flab.just_10_minutes.notification.service.channel;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.domain.Notification;
import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationDao;
import com.flab.just_10_minutes.notification.service.event.ChannelEvent;
import com.flab.just_10_minutes.notification.service.event.MockChannelEvent;
import com.flab.just_10_minutes.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockNotificationChannel implements NotificationChannel{

    private final NotificationDao notificationDao;

    @Override
    public Boolean isApplicable(User user) {
        return true;
    }

    @Override
    public void createNotification(User user, NotificationEvent event, Campaign campaign) {
        notificationDao.save(Notification.from(event.getEventId(),
                                            campaign.getId(),
                                            "test",
                                            getChannelType(),
                                            false));
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.MOCK;
    }

    @Override
    public ChannelEvent createChannelEvent(String eventId) {
        return MockChannelEvent.builder()
                            .eventId(eventId)
                            .channelType(getChannelType())
                            .build();
    }
}
