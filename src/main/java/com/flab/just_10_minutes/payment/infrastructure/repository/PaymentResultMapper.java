package com.flab.just_10_minutes.payment.infrastructure.repository;

import com.flab.just_10_minutes.payment.infrastructure.entity.PaymentResultEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentResultMapper {

    @Insert("INSERT INTO payment_results (imp_uid, merchant_uid, name, buyer_name, amount, currency, paid_at, pay_method, status, cancel_amount, cancel_reason, cancelled_at, fail_reason, failed_at) " +
            "VALUES (#{impUid}, #{merchantUid}, #{name}, #{buyerName}, #{amount}, #{currency}, #{paidAt}, #{payMethod}, #{status}, #{cancelAmount}, #{cancelReason}, #{cancelledAt}, #{failReason}, #{failedAt})")
    int save(PaymentResultEntity paymentResultEntity);


    @Select("SELECT * FROM payment_results " +
            "WHERE imp_uid = #{impUid}")
    PaymentResultEntity findByImpUid(final String impUid);

    @Select("""
            SELECT pr.id,
                    pr.imp_uid, 
                    pr.merchant_uid, 
                    pr.name, 
                    pr.buyer_name, 
                    pr.amount, 
                    pr.currency, 
                    pr.paid_at, 
                    pr.pay_method, 
                    pr.status, 
                    pr.cancel_amount, 
                    pr.cancel_reason, 
                    pr.cancelled_at, 
                    pr.fail_reason, 
                    pr.failed_at  FROM payment_results pr
            INNER JOIN orders o ON pr.imp_uid = o.payment_tx_id
            WHERE pr.imp_uid = #{impUid}
            """)
   PaymentResultEntity findWithOrderByImpUidAndStatus(final String impUid, final String status);
}
