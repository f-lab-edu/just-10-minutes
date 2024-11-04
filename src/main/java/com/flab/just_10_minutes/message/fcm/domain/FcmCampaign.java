package com.flab.just_10_minutes.message.fcm.domain;

import com.flab.just_10_minutes.message.fcm.dto.FcmCampaignRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FcmCampaign {

    private Long id;
    private String title;
    private String body;
    private String imgUrl;

    public static FcmCampaign from(FcmCampaignRequest fcmCampaignRequest) {
        return FcmCampaign.builder()
                        .title(fcmCampaignRequest.getTitle())
                        .body(fcmCampaignRequest.getBody())
                        .imgUrl(fcmCampaignRequest.getImgUrl())
                        .build();
    }
}
