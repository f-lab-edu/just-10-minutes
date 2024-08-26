package com.flab.just_10_minutes.Payment.infrastructure.repository;

import com.flab.just_10_minutes.Payment.infrastructure.entity.PaymentResultEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentResultMapper {

    @Insert("INSERT INTO payment_results (imp_uid, merchant_uid, name, buyer_name, amount, currency, paid_at, pay_method, status, cancel_amount, cancel_reason, cancelled_at, fail_reason, failed_at) " +
            "VALUES (#{impUid}, #{merchantUid}, #{name}, #{buyerName}, #{amount}, #{currency}, #{paidAt}, #{payMethod}, #{status}, #{cancelAmount}, #{cancelReason}, #{cancelledAt}, #{failReason}, #{failedAt})")
    public int save(PaymentResultEntity paymentResultEntity);


    @Select("SELECT * " +
            "FROM payment_results " +
            "WHERE imp_uid = #{impUid}")
    public PaymentResultEntity findByImpUid(final String impUid);
}
