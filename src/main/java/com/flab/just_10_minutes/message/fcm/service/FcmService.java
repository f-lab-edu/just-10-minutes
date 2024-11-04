package com.flab.just_10_minutes.message.fcm.service;

import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.domain.FcmToken;
import com.flab.just_10_minutes.message.fcm.dto.FcmCampaignRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmMessageRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmSendRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmTokenRequest;
import com.flab.just_10_minutes.message.fcm.infrastructure.FcmApiClient;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmCampaignDao;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmMessageDao;
import com.flab.just_10_minutes.message.fcm.infrastructure.repository.FcmTokenDao;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.flab.just_10_minutes.util.common.IDUtil.issueFcmMessageId;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenDao fcmTokenDao;
    private final FcmMessageDao fcmMessageDao;
    private final FcmCampaignDao fcmCampaignDao;
    private final UserDao userDao;
    private final FcmApiClient fcmApiClient;

    public void saveToken(FcmTokenRequest fcmTokenRequest) {
        //TODO : 유저가 여러개 디바이스를 등록할 때 처리
       // userDao.fetch(fcmTokenRequest.getLoginId());

        fcmTokenDao.save(FcmToken.from(fcmTokenRequest));
    }

    public void saveCampaign(FcmCampaignRequest fcmCampaignRequest) {
        fcmCampaignDao.save(FcmCampaign.from(fcmCampaignRequest));
    }

    public FcmMessage saveMessage(FcmMessageRequest fcmMessageRequest) {
        //TODO : 유저가 여러개의 디바이스 토큰을 가지고 있을 때 처리
        FcmToken fcmToken = fcmTokenDao.fetchByLoginId(fcmMessageRequest.getLoginId());
        FcmCampaign fcmCampaign = fcmCampaignDao.fetchById(fcmMessageRequest.getCampaignId());

        return fcmMessageDao.save(FcmMessage.create(issueFcmMessageId(), fcmToken, fcmCampaign));
    }

    public void sendNotification(FcmSendRequest fcmSendRequest) {
        FcmMessage fcmMessage = fcmMessageDao.fetchByMessageId(fcmSendRequest.getMessageId());
        FcmCampaign fcmCampaign = fcmCampaignDao.fetchById(fcmMessage.getCampaignId());

        fcmApiClient.sendMessage(fcmMessage, fcmCampaign);

        fcmMessageDao.patch(FcmMessage.update(fcmSendRequest.getMessageId(), true));
    }
}
