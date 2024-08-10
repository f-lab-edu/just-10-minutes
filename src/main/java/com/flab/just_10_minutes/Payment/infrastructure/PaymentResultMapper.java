package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentResultMapper {

    @Insert("INSERT INTO payment_results_table (payment_tx_id, order_id, order_name, buyer_name, amount, currency, paid_at, pay_method, status, cancel_amount, cancel_reason, cancelled_at, fail_reason, failed_at) " +
            "VALUES (#{paymentTxId}, #{orderId}, #{orderName}, #{buyerName}, #{amount}, #{currency}, #{paidAt}, #{payMethod}, #{status}, #{cancelAmount}, #{cancelReason}, #{cancelledAt}, #{failReason}, #{failedAt})")
    public int save(PaymentResult paymentResult);


    @Select("SELECT * " +
            "FROM payment_results_table " +
            "WHERE payment_tx_id = #{paymentTxId}")
    public PaymentResult findByPaymentTxId(final String paymentTxId);
}
