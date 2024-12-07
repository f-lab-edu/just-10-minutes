package com.flab.just_10_minutes.notification.service.eventListener;

import com.flab.just_10_minutes.common.exception.business.BusinessException;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.FcmNotification;
import com.flab.just_10_minutes.notification.domain.FcmNotificationEvent;
import com.flab.just_10_minutes.notification.domain.FcmToken;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.FcmApiClient;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.request.FcmApiV1Request;
import com.flab.just_10_minutes.notification.infrastructure.fcmAPiV1.response.FcmApiV1Response;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmNotificationDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmTokenDao;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublishFcmNotificationSqsListener {

    private final FcmNotificationDao fcmNotificationDao;
    private final CampaignDao campaignDao;
    private final FcmTokenDao fcmTokenDao;
    private final FcmApiClient fcmApiClient;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.queue-name.notification-event}",
            factory = "sqsMessageListenerContainerFactory",
            messageVisibilitySeconds = "1"
    )
    @Retryable(
            value = {HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleNotificationEvent(FcmNotificationEvent event) {
        FcmToken fcmToken = fcmTokenDao.fetchByLoginId(event.getReceiverId());
        Campaign campaign = campaignDao.fetchById(event.getCampaignId());

        FcmApiV1Request fcmApiV1Request = FcmApiV1Request.from(FcmNotification.from(event, fcmToken), campaign);
        FcmApiV1Response fcmApiV1Response = fcmApiClient.sendMessage(fcmApiV1Request);
        handleFcmResponse(fcmApiV1Response, fcmApiV1Request);
    }

    private void handleFcmResponse(FcmApiV1Response fcmApiV1Response, FcmApiV1Request fcmApiV1Request) {
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
            case 4:
                //TODO: 429일 때는 실패 처리가 아닌 재전송 필요
                if (fcmApiV1Response.getCode() == 404) {
                    try {
                        fcmTokenDao.delete(fcmApiV1Request.getMessage().getToken());
                    } catch (InternalException ie) {
                        log.info("Fail to delete FCM token: {}", fcmApiV1Request.getMessage().getToken());
                    }
                }
                log.error("Invalid Argument Error Occurred from FCM. error code : {}, message: {}, request: {}", fcmApiV1Response.getCode(), fcmApiV1Request.getMessage(), fcmApiV1Request.toString());
                break;
            case 5:
                throw new HttpServerErrorException(HttpStatus.valueOf(fcmApiV1Response.getCode()), fcmApiV1Response.getMessage());
        }
    }

    @Recover
    public void recover(HttpServerErrorException e, FcmNotificationEvent event) {
        log.error("Failed to send FCM notification after 3 times retries. Event: {}, Error: {}", event, e.getMessage());
        throw new BusinessException("Failed after retries: " + e.getMessage());
    }
}
