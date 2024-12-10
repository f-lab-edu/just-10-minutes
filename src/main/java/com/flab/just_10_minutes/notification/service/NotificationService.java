package com.flab.just_10_minutes.notification.service;

import com.flab.just_10_minutes.common.exception.business.BusinessException;
import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.FcmToken;
import com.flab.just_10_minutes.notification.domain.FcmNotificationEvent;
import com.flab.just_10_minutes.notification.dto.CampaignRequest;
import com.flab.just_10_minutes.notification.dto.FcmNotificationRequest;
import com.flab.just_10_minutes.notification.dto.FcmTokenRequest;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmTokenDao;
import io.awspring.cloud.sqs.operations.MessagingOperationFailedException;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.common.util.IDUtil.issueEventId;
import static com.flab.just_10_minutes.common.util.IDUtil.issueUUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmTokenDao fcmTokenDao;
    private final CampaignDao campaignDao;
    private final ApplicationEventPublisher eventPublisher;
    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue-name.notification-event-fifo}")
    private String notificationQueueFIFO;

    public void saveToken(FcmTokenRequest fcmTokenRequest) {
        //TODO : 유저가 여러개 디바이스를 등록할 때 처리
       // userDao.fetch(fcmTokenRequest.getLoginId());

        fcmTokenDao.save(FcmToken.from(fcmTokenRequest));
    }

    public void saveCampaign(CampaignRequest campaignRequest) {
        campaignDao.save(Campaign.from(campaignRequest));
    }

    @Transactional
    public void publishFcmNotificationEvent(FcmNotificationRequest fcmNotificationRequest) {
        FcmToken fcmToken = fcmTokenDao.fetchByLoginId(fcmNotificationRequest.getLoginId());
        Campaign campaign = campaignDao.fetchById(fcmNotificationRequest.getCampaignId());

        send(FcmNotificationEvent.from(issueEventId(),
                        fcmToken.getLoginId(),
                        campaign.getId()));
    }

    private void send(FcmNotificationEvent event) {
        try {
            SendResult<Object> sendResult = sqsTemplate.send(to -> {
                to.queue(notificationQueueFIFO);
                to.messageDeduplicationId(issueUUID().toString());
                //TODO: MessageGroupId 설정
                to.payload(event);
            });
        } catch (MessagingOperationFailedException moe) {
            log.error("Fail to send SQS. endpoint: {}, msg: {}, cause: {}", moe.getEndpoint(), moe.getMessage(), moe.getCause().getMessage());
            throw new BusinessException("Fail to send Notification. message: " + moe.getMessage());
        }
    }
}
