package com.flab.just_10_minutes.notification.service.channel;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.domain.Notification;
import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmTokenDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationDao;
import com.flab.just_10_minutes.notification.service.event.ChannelEvent;
import com.flab.just_10_minutes.notification.service.event.FcmChannelEvent;
import com.flab.just_10_minutes.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmNotificationChannel implements NotificationChannel{

    private final NotificationDao notificationDao;
    private final FcmTokenDao fcmTokenDao;

    @Override
    public Boolean isApplicable(User user) {
        return fcmTokenDao.fetchByLoginId(user.getLoginId()) != null;
    }

    @Override
    public void createNotification(User user, NotificationEvent event, Campaign campaign) {
        notificationDao.save(Notification.from(event.getEventId(),
                                            campaign.getId(),
                                            fcmTokenDao.fetchByLoginId(user.getLoginId()).getToken(),
                                            getChannelType(),
                                            false));
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.FCM;
    }

    @Override
    public ChannelEvent createChannelEvent(String eventId) {
        return FcmChannelEvent.builder()
                            .eventId(eventId)
                            .channelType(getChannelType())
                            .build();
    }
}
