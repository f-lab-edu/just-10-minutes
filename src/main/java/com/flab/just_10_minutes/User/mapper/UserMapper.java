package com.flab.just_10_minutes.User.mapper;

import com.flab.just_10_minutes.User.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users_table WHERE login_id = #{loginId}")
    User findByLoginId(final String loginId);

    @Insert("INSERT INTO users_table (login_id, password, phone, address, role)" +
            "VALUES (#{loginId}, #{password}, #{phone}, #{address}, #{role})")
    int save(final User user);
}
