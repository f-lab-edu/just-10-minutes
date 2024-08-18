package com.flab.just_10_minutes.Payment.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CustomUidMapper {

    @Insert("INSERT INTO custom_uids (login_id, custom_uid) VALUES (#{loginId}, #{customUid})")
    public int save(CustomUidEntity customUidEntity);

    @Select("SELECT custom_uid FROM custom_uids WHERE login_id = #{loginId}")
    public String findByLoginId(final String loginId);
}
