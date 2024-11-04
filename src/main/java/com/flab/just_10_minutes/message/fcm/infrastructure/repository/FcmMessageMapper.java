package com.flab.just_10_minutes.message.fcm.infrastructure.repository;

import com.flab.just_10_minutes.message.fcm.infrastructure.entity.FcmMessageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FcmMessageMapper {

    @Insert("INSERT INTO fcm_messages (message_id, token, campaign_id, is_send) VALUES (#{messageId}, #{token}, #{campaignId}, #{isSend})")
    int save(FcmMessageEntity fcmMessageEntity);

    @Select("SELECT * FROM fcm_messages WHERE message_id = #{messageId}")
    FcmMessageEntity findByMessageId(final String messageId);

    @Update("""
            <script>
            UPDATE fcm_message
            <set>
                <if test='token != null'> token = #{token},</if>
                <if test='campaignId != null'> campaign_id = #{campaignId},</if>
                <if test='isSend != null'> is_send = #{isSend},</if>
            </set>
            WHERE message_id = #{messageId}
            </script>
            """)
    int patch(FcmMessageEntity fcmMessageEntity);
}
