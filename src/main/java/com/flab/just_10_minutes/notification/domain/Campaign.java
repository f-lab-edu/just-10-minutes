package com.flab.just_10_minutes.notification.domain;

import com.flab.just_10_minutes.notification.dto.CampaignRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Campaign {

    private Long id;
    private String title;
    private String body;
    private String imgUrl;

    public static Campaign from(CampaignRequest campaignRequest) {
        return Campaign.builder()
                        .title(campaignRequest.getTitle())
                        .body(campaignRequest.getBody())
                        .imgUrl(campaignRequest.getImgUrl())
                        .build();
    }
}
