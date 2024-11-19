package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.infrastructure.entity.NotificationEventEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NotificationEventMapper {

    @Insert("INSERT INTO notification_events (event_id, receiver_id, campaign_id, is_published) VALUES (#{eventId}, #{receiverId}, #{campaignId}, #{isPublished})")
    int save(NotificationEventEntity fcmMessageEntity);

    @Select("SELECT * FROM notification_events WHERE event_id = #{eventId}")
    NotificationEventEntity findByMessageId(final String messageId);

    @Update("""
            <script>
            UPDATE notification_events
            <set>
                <if test='receiverId != null'> receiver_id = #{receiverId},</if>
                <if test='campaignId != null'> campaign_id = #{campaignId},</if>
                <if test='isPublished != null'> is_published = #{isPublished},</if>
            </set>
            WHERE event_id = #{eventId}
            </script>
            """)
    int patch(NotificationEventEntity notificationEventEntity);
}
