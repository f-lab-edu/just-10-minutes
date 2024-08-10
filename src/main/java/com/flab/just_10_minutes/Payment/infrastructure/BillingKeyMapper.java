package com.flab.just_10_minutes.Payment.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BillingKeyMapper {

    @Insert("INSERT INTO billing_keys_table (login_id, billing_key) VALUES (#{loginId}, #{billingKey})")
    public int save(final String loginId, final String billingKey);

    @Select("SELECT billing_key FROM billing_keys_table WHERE login_id = #{loginId}")
    public String findByLoginId(final String loginId);
}
