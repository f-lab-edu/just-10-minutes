package com.flab.just_10_minutes.notification.infrastructure.entity;

import com.flab.just_10_minutes.notification.domain.Campaign;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CampaignEntity {

    private Long id;
    private String title;
    private String body;
    private String imgUrl;

    public static CampaignEntity from(Campaign fcmCampaign) {
        return CampaignEntity.builder()
                    .title(fcmCampaign.getTitle())
                    .body(fcmCampaign.getBody())
                    .imgUrl(fcmCampaign.getImgUrl())
                    .build();
    }

    public static Campaign toDomain(CampaignEntity fcmCampaignEntity) {
        return Campaign.builder()
                        .id(fcmCampaignEntity.getId())
                        .title(fcmCampaignEntity.getTitle())
                        .body(fcmCampaignEntity.getBody())
                        .imgUrl(fcmCampaignEntity.getImgUrl())
                        .build();
    }
}
