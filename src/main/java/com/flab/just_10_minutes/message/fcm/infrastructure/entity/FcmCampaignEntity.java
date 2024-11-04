package com.flab.just_10_minutes.message.fcm.infrastructure.entity;

import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmCampaignEntity {

    private Long id;
    private String title;
    private String body;
    private String imgUrl;

    public static FcmCampaignEntity from(FcmCampaign fcmCampaign) {
        return FcmCampaignEntity.builder()
                    .title(fcmCampaign.getTitle())
                    .body(fcmCampaign.getBody())
                    .imgUrl(fcmCampaign.getImgUrl())
                    .build();
    }

    public static FcmCampaign toDomain(FcmCampaignEntity fcmCampaignEntity) {
        return FcmCampaign.builder()
                        .id(fcmCampaignEntity.getId())
                        .title(fcmCampaignEntity.getTitle())
                        .body(fcmCampaignEntity.getBody())
                        .imgUrl(fcmCampaignEntity.getImgUrl())
                        .build();
    }
}
