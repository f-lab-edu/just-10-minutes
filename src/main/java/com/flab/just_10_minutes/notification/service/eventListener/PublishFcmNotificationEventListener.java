package com.flab.just_10_minutes.notification.service.eventListener;

import com.flab.just_10_minutes.notification.domain.*;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.FcmApiClient;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.request.FcmApiV1Request;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.response.FcmApiV1Response;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmNotificationDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmTokenDao;
import com.flab.just_10_minutes.common.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.HttpServerErrorException;

import static com.flab.just_10_minutes.common.executor.AsyncConfig.EVENT_HANDLER_TASK_EXECUTOR;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublishFcmNotificationEventListener {

    private final FcmNotificationDao fcmNotificationDao;
    private final CampaignDao campaignDao;
    private final FcmTokenDao fcmTokenDao;
    private final FcmApiClient fcmApiClient;

    @Async(EVENT_HANDLER_TASK_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            value = {HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleFcmNotificationEvent(FcmNotificationEvent event) {
        FcmToken fcmToken = fcmTokenDao.fetchByLoginId(event.getReceiverId());
        Campaign campaign = campaignDao.fetchById(event.getCampaignId());
        FcmNotification fcmNotification = FcmNotification.from(event, fcmToken);

        FcmApiV1Request fcmApiV1Request = FcmApiV1Request.from(FcmNotification.from(event, fcmToken), campaign);
        FcmApiV1Response fcmApiV1Response = fcmApiClient.sendMessage(fcmApiV1Request);
        handleFcmResponse(fcmApiV1Response, fcmNotification);
    }

    private void handleFcmResponse(FcmApiV1Response fcmApiV1Response, FcmNotification fcmNotification) {
            /*
            401 : UNAUTHENTICATED(Google Credential error)
            400 : invalid argument
            403 : permission denied
            404 : NOT FOUND(token not found)
            429 : quota exceeded(메시지 대상에 대한 전송 제한 초과)
            504 : unavailable(서버 과부하)
            500 : internal
             */
        switch (fcmApiV1Response.getCode() / 100) {
            case 2:
                fcmNotificationDao.save(fcmNotification);
                break;
            case 5:
                throw new HttpServerErrorException(HttpStatus.valueOf(fcmApiV1Response.getCode()), fcmApiV1Response.getMessage());
            default:
                throw new BusinessException("Failed to send FCM notification. Cause: " + fcmApiV1Response.getMessage());
        }
    }

    @Recover
    public void recover(HttpServerErrorException e, FcmNotificationEvent event) {
        log.error("Failed to send FCM notification after 3 times retries. Event: {}, Error: {}", event, e.getMessage());
        throw new BusinessException("Failed after retries: " + e.getMessage());
    }
}
