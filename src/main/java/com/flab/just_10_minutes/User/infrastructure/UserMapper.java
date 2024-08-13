package com.flab.just_10_minutes.User.infrastructure;

import com.flab.just_10_minutes.User.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users_table WHERE login_id = #{loginId}")
    User findByLoginId(final String loginId);

    @Insert("INSERT INTO users_table (login_id, password, phone, address, role, point)" +
            "VALUES (#{loginId}, #{password}, #{phone}, #{address}, #{role}, #{point})")
    int save(final User user);

    @Select("SELECT EXISTS(SELECT * FROM users_table WHERE login_id = #{loginId});")
    boolean existsByLoginId(final String loginId);

    @Update("UPDATE users_table SET point = #{updatePoints} WHERE login_id = #{loginId}")
    int updatePoint(final String loginId, final Long updatePoints);

    @Delete("DELETE FROM users_table WHERE login_id = #{loginId}")
    public int delete(final String loginId);

}
