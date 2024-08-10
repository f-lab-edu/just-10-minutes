package com.flab.just_10_minutes.Order.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders_table (order_id, seller_login_id, buyer_login_id, used_point, buy_quantity, total_price, refunded_price, payment_tx_id, status) " +
            "VALUES (#{orderId}, #{sellerLoginId}, #{buyerLoginId}, #{usedPoint}, #{buyQuantity}, #{totalPrice}, #{refundedPrice}, #{paymentTxId}, #{status})")
    public int save(OrderEntity orderEntity);

    @Select("SELECT order_id, seller_login_id, buyer_login_id, used_point, buy_quantity, total_price, refunded_price, payment_tx_id, status " +
            "FROM orders_table " +
            "WHERE order_id = #{orderId}")
    public OrderEntity findByOrderId(final String orderId);
}
