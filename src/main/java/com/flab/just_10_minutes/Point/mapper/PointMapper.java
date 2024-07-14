package com.flab.just_10_minutes.Point.mapper;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PointMapper {

    @Insert("INSERT INTO point_histories_table (login_id, quantity, reason)" +
            "VALUES  (#{loginId}, #{quantity}, #{reason})")
    int save(PointHistory pointHistory);

    @Select("SELECT * FROM point_histories_table WHERE login_id = #{loginId}")
    List<PointHistory> findByLoginId(final String loginId);
}
