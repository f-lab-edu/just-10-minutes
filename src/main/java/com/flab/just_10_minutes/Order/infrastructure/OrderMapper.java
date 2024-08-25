package com.flab.just_10_minutes.Order.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (id, seller_login_id, buyer_login_id, used_point, buy_quantity, total_price, refunded_price, payment_tx_id, status) " +
            "VALUES (#{id}, #{sellerLoginId}, #{buyerLoginId}, #{usedPoint}, #{buyQuantity}, #{totalPrice}, #{refundedPrice}, #{paymentTxId}, #{status})")
    public int save(OrderEntity orderEntity);

    @Select("SELECT id, seller_login_id, buyer_login_id, used_point, buy_quantity, total_price, refunded_price, payment_tx_id, status " +
            "FROM orders " +
            "WHERE id = #{id}")
    public OrderEntity findById(final String id);
}
