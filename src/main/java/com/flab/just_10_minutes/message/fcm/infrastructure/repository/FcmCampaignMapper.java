package com.flab.just_10_minutes.message.fcm.infrastructure.repository;

import com.flab.just_10_minutes.message.fcm.infrastructure.entity.FcmCampaignEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FcmCampaignMapper {

    @Insert("INSERT INTO fcm_campaigns (title, body, img_url) VALUES (#{title}, #{body}, #{imgUrl})")
    int save(FcmCampaignEntity fcmCampaignEntity);

    @Select("SELECT * FROM fcm_campaigns WHERE id = #{id}")
    FcmCampaignEntity findByLoginId(final Long id);
}
