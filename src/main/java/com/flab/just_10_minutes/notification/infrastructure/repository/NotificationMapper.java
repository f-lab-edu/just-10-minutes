package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.infrastructure.entity.NotificationEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NotificationMapper {

    @Insert("""
            INSERT INTO notifications (event_id,
                                       campaign_id,
                                       destination,
                                       channel_type,
                                       is_send)
                   VALUES (#{eventId},
                            #{campaignId},
                            #{destination},
                            #{channelType},
                            #{isSend})
            """)
    int save(NotificationEntity notificationEntity);

    @Select("""
            SELECT * FROM notifications
            WHERE event_id = #{eventId}
            AND channel_type = #{channelType}
            """)
    NotificationEntity findByEventIdAndChannelType(final String eventId, final ChannelType channelType);

    @Update("""
            <script>
            UPDATE notifications
            <set>
                <if test='isSend != null'> is_send = #{isSend},</if>
            </set>
            WHERE event_id = #{eventId}
            AND channel_type = #{channelType}
            </script>
            """)
    int patch(NotificationEntity notificationEntity);
}
