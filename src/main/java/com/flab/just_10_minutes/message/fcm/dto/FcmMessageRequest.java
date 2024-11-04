package com.flab.just_10_minutes.message.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessageRequest {

    private String loginId;
    private Long campaignId;
}
