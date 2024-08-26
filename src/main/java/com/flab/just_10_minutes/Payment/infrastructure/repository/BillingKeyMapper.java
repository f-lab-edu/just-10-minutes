package com.flab.just_10_minutes.Payment.infrastructure.repository;

import com.flab.just_10_minutes.Payment.infrastructure.entity.BillingKeyEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BillingKeyMapper {

    @Insert("INSERT INTO billing_keys (login_id, customer_uid) VALUES (#{loginId}, #{customerUid})")
    public int save(BillingKeyEntity billingKeyEntity);

    @Select("SELECT customer_uid FROM billing_keys WHERE login_id = #{loginId}")
    public String findByLoginId(final String loginId);
}
