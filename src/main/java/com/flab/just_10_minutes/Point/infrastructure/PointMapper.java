package com.flab.just_10_minutes.Point.infrastructure;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PointMapper {

    @Insert("INSERT INTO point_histories_table (login_id, quantity, reason, total_quantity) " +
            "VALUES  (#{loginId}, #{quantity}, #{reason}, #{totalQuantity})")
    int save(PointHistory pointHistory);

    @Select("SELECT login_id, quantity, reason, total_quantity " +
            "FROM point_histories_table " +
            "WHERE login_id = #{loginId} " +
            "ORDER BY id DESC " +
            "LIMIT 1")
    PointHistory findTopByOrderByLoginIdDesc(final String loginId);

    @Select("SELECT login_id, quantity, reason, total_quantity " +
            "FROM point_histories_table " +
            "WHERE login_id = #{loginId}")
    List<PointHistory> findByLoginId(final String loginId);
}
