package com.flab.just_10_minutes.message.fcm.service;

import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.domain.FcmToken;
import com.flab.just_10_minutes.message.fcm.dto.*;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmCampaignDao;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmMessageDao;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmTokenDao;
import com.flab.just_10_minutes.message.fcm.service.event.PublishMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.util.common.IDUtil.issueFcmMessageId;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenDao fcmTokenDao;
    private final FcmMessageDao fcmMessageDao;
    private final FcmCampaignDao fcmCampaignDao;
    private final ApplicationEventPublisher eventPublisher;

    public void saveToken(FcmTokenRequest fcmTokenRequest) {
        //TODO : 유저가 여러개 디바이스를 등록할 때 처리
       // userDao.fetch(fcmTokenRequest.getLoginId());

        fcmTokenDao.save(FcmToken.from(fcmTokenRequest));
    }

    public void saveCampaign(FcmCampaignRequest fcmCampaignRequest) {
        fcmCampaignDao.save(FcmCampaign.from(fcmCampaignRequest));
    }

    @Transactional
    public void publishNotification(FcmMessageRequest fcmMessageRequest) {
        //TODO : 유저가 여러개의 디바이스 토큰을 가지고 있을 때 처리
        FcmToken fcmToken = fcmTokenDao.fetchByLoginId(fcmMessageRequest.getLoginId());
        FcmCampaign fcmCampaign = fcmCampaignDao.fetchById(fcmMessageRequest.getCampaignId());

        FcmMessage fcmMessage = fcmMessageDao.save(FcmMessage.create(issueFcmMessageId(), fcmToken, fcmCampaign));
        eventPublisher.publishEvent(PublishMessageEvent.builder()
                                                    .messageId(fcmMessage.getMessageId())
                                                    .build());
    }
}
