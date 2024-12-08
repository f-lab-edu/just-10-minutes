package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.infrastructure.entity.FcmTokenEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FcmTokenMapper {

    @Insert("""
            INSERT INTO fcm_tokens
                (login_id, token, created_at)
            VALUES
                (#{loginId}, #{token}, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE
                token = #{token},
                updated_at = CURRENT_TIMESTAMP;
            """)
    int upsert(FcmTokenEntity fcmTokenEntity);

    @Select("SELECT * FROM fcm_tokens WHERE login_id = #{loginId}")
    FcmTokenEntity findByLoginId(final String loginId);

    @Delete("DELETE FROM fcm_tokens WHERE token = #{token}")
    int delete(final String token);
}
