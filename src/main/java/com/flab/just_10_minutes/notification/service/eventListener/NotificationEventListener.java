package com.flab.just_10_minutes.notification.service.eventListener;

import com.flab.just_10_minutes.notification.domain.*;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationEventDao;
import com.flab.just_10_minutes.notification.service.channel.NotificationChannel;
import com.flab.just_10_minutes.notification.service.event.PublishNotificationEvent;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

import static com.flab.just_10_minutes.util.executor.AsyncConfig.EVENT_HANDLER_TASK_EXECUTOR;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationEventDao notificationEventDao;
    private final CampaignDao fcmCampaignDao;
    private final UserDao userDao;
    private final List<NotificationChannel> notificationChannels;

    private final ApplicationEventPublisher eventPublisher;

    @Async(EVENT_HANDLER_TASK_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePublishNotificationEvent(PublishNotificationEvent event) {
        NotificationEvent notificationEvent = notificationEventDao.fetchByEventId(event.getEventId());
        Campaign campaign = fcmCampaignDao.fetchById(notificationEvent.getCampaignId());
        User user = userDao.fetch(notificationEvent.getReceiverId());

        for(NotificationChannel channel : notificationChannels) {
            if (channel.isApplicable(user)) {
                channel.createNotification(user, notificationEvent, campaign);

                eventPublisher.publishEvent(channel.createChannelEvent(event.getEventId()));
            }
        }

        notificationEventDao.patch(notificationEvent.update(true));
    }
}
