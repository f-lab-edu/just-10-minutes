package com.flab.just_10_minutes.notification.controller;

import com.flab.just_10_minutes.notification.dto.CampaignRequest;
import com.flab.just_10_minutes.notification.dto.FcmNotificationRequest;
import com.flab.just_10_minutes.notification.dto.FcmTokenRequest;
import com.flab.just_10_minutes.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/fcm-tokens")
    public ResponseEntity<HttpStatus> saveToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
        notificationService.saveToken(fcmTokenRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/campaigns")
    public ResponseEntity<HttpStatus> saveCampaign(@RequestBody CampaignRequest campaignRequest) {
        notificationService.saveCampaign(campaignRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/fcm-publish")
    public void publishMessage(@RequestBody FcmNotificationRequest fcmNotificationRequest) {
        notificationService.publishFcmNotificationEvent(fcmNotificationRequest);
    }
}
