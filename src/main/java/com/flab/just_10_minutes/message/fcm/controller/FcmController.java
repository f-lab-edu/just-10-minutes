package com.flab.just_10_minutes.message.fcm.controller;

import com.flab.just_10_minutes.message.fcm.dto.FcmCampaignRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmMessageRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmSendRequest;
import com.flab.just_10_minutes.message.fcm.dto.FcmTokenRequest;
import com.flab.just_10_minutes.message.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm-messages")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/tokens")
    public void saveToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
        fcmService.saveToken(fcmTokenRequest);
    }

    @PostMapping("/campaigns")
    public void saveCampaign(@RequestBody FcmCampaignRequest fcmCampaignRequest) {
        fcmService.saveCampaign(fcmCampaignRequest);
    }

    @PostMapping
    public void saveMessage(@RequestBody FcmMessageRequest fcmMessageRequest) {
        fcmService.saveMessage(fcmMessageRequest);
    }

    @PostMapping("/single-notifications")
    public void sendMessage(@RequestBody FcmSendRequest fcmSendRequest) {
        fcmService.sendNotification(fcmSendRequest);
    }
}
