package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.infrastructure.entity.FcmNotificationEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FcmNotificationMapper {

    @Insert("""
            INSERT INTO fcm_notifications (notification_id,
                                       campaign_id,
                                       destination,
                                       is_send)
                   VALUES (#{notificationId},
                            #{campaignId},
                            #{destination},
                            #{isSend})
            """)
    int save(FcmNotificationEntity notificationEntity);

    @Select("""
            SELECT * FROM fcm_notifications
            WHERE notification_id = #{notificationId}
            """)
    FcmNotificationEntity findByNotificationId(final String notificationId);
}
