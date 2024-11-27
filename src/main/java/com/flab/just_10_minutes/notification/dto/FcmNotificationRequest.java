package com.flab.just_10_minutes.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmNotificationRequest {

    private String loginId;
    private Long campaignId;
}
