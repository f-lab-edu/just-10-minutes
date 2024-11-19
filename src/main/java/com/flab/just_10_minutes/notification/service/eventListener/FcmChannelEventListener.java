package com.flab.just_10_minutes.notification.service.eventListener;

import com.flab.just_10_minutes.notification.domain.*;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.FcmApiClient;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.response.FcmApiV1Response;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationDao;
import com.flab.just_10_minutes.notification.service.event.FcmChannelEvent;
import com.flab.just_10_minutes.util.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.flab.just_10_minutes.util.executor.AsyncConfig.EVENT_HANDLER_TASK_EXECUTOR;

@Component
@RequiredArgsConstructor
public class FcmChannelEventListener {

    private final NotificationDao notificationDao;
    private final CampaignDao fcmCampaignDao;

    private final FcmApiClient fcmApiClient;

    @Async(EVENT_HANDLER_TASK_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFcmChannelEvent(FcmChannelEvent event) {
        Notification notification = notificationDao.fetchByEventIdAndChannelType(event.getEventId(), event.getChannelType());
        Campaign campaign = fcmCampaignDao.fetchById(notification.getCampaignId());

        FcmApiV1Response fcmApiV1Response = fcmApiClient.sendMessage(notification, campaign);
        if (fcmApiV1Response.getCode() != 200) {
            /*TODO : error status에 따른 재시도 처리

            401 : Google Credential error
            400 : invalid argument
            403 : permission denied
            429 : quota exceeded(메시지 대상에 대한 전송 제한 초과)
            504 : unavailable(서버 과부하)
            500 : internal
             */
            throw new BusinessException("Failed to reason : " + fcmApiV1Response.getMessage());
        }

        notificationDao.patch(notification.update(true));
    }
}
