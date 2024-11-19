package com.flab.just_10_minutes.notification.service;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.domain.FcmToken;
import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import com.flab.just_10_minutes.notification.dto.CampaignRequest;
import com.flab.just_10_minutes.notification.dto.NotificationRequest;
import com.flab.just_10_minutes.notification.dto.FcmTokenRequest;
import com.flab.just_10_minutes.notification.infrastructure.repository.CampaignDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.FcmTokenDao;
import com.flab.just_10_minutes.notification.infrastructure.repository.NotificationEventDao;
import com.flab.just_10_minutes.notification.service.event.PublishNotificationEvent;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.util.common.IDUtil.issueEventId;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmTokenDao fcmTokenDao;
    private final NotificationEventDao notificationEventDao;
    private final CampaignDao campaignDao;
    private final UserDao userDao;
    private final ApplicationEventPublisher eventPublisher;

    public void saveToken(FcmTokenRequest fcmTokenRequest) {
        //TODO : 유저가 여러개 디바이스를 등록할 때 처리
       // userDao.fetch(fcmTokenRequest.getLoginId());

        fcmTokenDao.save(FcmToken.from(fcmTokenRequest));
    }

    public void saveCampaign(CampaignRequest campaignRequest) {
        campaignDao.save(Campaign.from(campaignRequest));
    }

    @Transactional
    public void publishNotificationEvent(NotificationRequest notificationRequest) {
        User user = userDao.fetch(notificationRequest.getLoginId());
        Campaign campaign = campaignDao.fetchById(notificationRequest.getCampaignId());

        NotificationEvent event = notificationEventDao.save(NotificationEvent.from(issueEventId(), notificationRequest.getLoginId(), notificationRequest.getCampaignId()));
        eventPublisher.publishEvent(PublishNotificationEvent.builder()
                                                            .eventId(event.getEventId())
                                                            .build());
    }
}
