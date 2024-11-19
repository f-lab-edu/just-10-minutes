package com.flab.just_10_minutes.notification.service.eventListener;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.Notification;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationDao;
import com.flab.just_10_minutes.notification.service.event.MockChannelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.flab.just_10_minutes.util.executor.AsyncConfig.EVENT_HANDLER_TASK_EXECUTOR;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockChannelEventListener {

    private final NotificationDao notificationDao;
    private final CampaignDao fcmCampaignDao;

    @Async(EVENT_HANDLER_TASK_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFcmChannelEvent(MockChannelEvent event) {
        Notification notification = notificationDao.fetchByEventIdAndChannelType(event.getEventId(), event.getChannelType());
        Campaign campaign = fcmCampaignDao.fetchById(notification.getCampaignId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("mock destination received");

        notificationDao.patch(notification.update(true));
    }
}
