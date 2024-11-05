package com.flab.just_10_minutes.message.fcm.controller;

import com.flab.just_10_minutes.message.fcm.dto.*;
import com.flab.just_10_minutes.message.fcm.service.FcmService;
import com.google.api.Http;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HttpStatus> saveToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
        fcmService.saveToken(fcmTokenRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/campaigns")
    public ResponseEntity<HttpStatus> saveCampaign(@RequestBody FcmCampaignRequest fcmCampaignRequest) {
        fcmService.saveCampaign(fcmCampaignRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<FcmMessageDto> saveMessage(@RequestBody FcmMessageRequest fcmMessageRequest) {
        return ResponseEntity.ok(fcmService.saveMessage(fcmMessageRequest));
    }

    @PostMapping("/single-notifications")
    public ResponseEntity<Http> sendMessage(@RequestBody FcmSendRequest fcmSendRequest) {
        fcmService.sendNotification(fcmSendRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
