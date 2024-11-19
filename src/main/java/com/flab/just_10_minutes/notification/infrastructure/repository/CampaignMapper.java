package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.infrastructure.entity.CampaignEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CampaignMapper {

    @Insert("INSERT INTO campaigns (title, body, img_url) VALUES (#{title}, #{body}, #{imgUrl})")
    int save(CampaignEntity fcmCampaignEntity);

    @Select("SELECT * FROM campaigns WHERE id = #{id}")
    CampaignEntity findByLoginId(final Long id);
}
