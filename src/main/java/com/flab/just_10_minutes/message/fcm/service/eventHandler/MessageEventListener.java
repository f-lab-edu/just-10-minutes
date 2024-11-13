package com.flab.just_10_minutes.message.fcm.service.eventHandler;

import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.FcmApiClient;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.response.FcmApiV1Response;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmCampaignDao;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmMessageDao;
import com.flab.just_10_minutes.message.fcm.service.event.PublishMessageEvent;
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
public class MessageEventListener {

    private final FcmMessageDao fcmMessageDao;
    private final FcmCampaignDao fcmCampaignDao;
    private final FcmApiClient fcmApiClient;

    @Async(EVENT_HANDLER_TASK_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePublishNotificationEvent(PublishMessageEvent event) {
        FcmMessage fcmMessage = fcmMessageDao.fetchByMessageId(event.getMessageId());
        FcmCampaign fcmCampaign = fcmCampaignDao.fetchById(fcmMessage.getCampaignId());

        FcmApiV1Response fcmApiV1Response = fcmApiClient.sendMessage(fcmMessage, fcmCampaign);
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
        fcmMessageDao.patch(FcmMessage.builder()
                                    .build()
                                    .withMessageId(event.getMessageId())
                                    .withIsSend(true));
    }
}
