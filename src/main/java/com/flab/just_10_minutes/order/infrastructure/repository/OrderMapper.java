package com.flab.just_10_minutes.order.infrastructure.repository;

import com.flab.just_10_minutes.order.infrastructure.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (id, seller_login_id, buyer_login_id, product_id, buy_quantity, used_point, total_price, refunded_price, payment_tx_id, status) " +
            "VALUES (#{id}, #{sellerLoginId}, #{buyerLoginId}, #{productId}, #{buyQuantity}, #{usedPoint}, #{totalPrice}, #{refundedPrice}, #{paymentTxId}, #{status})")
    int save(OrderEntity orderEntity);

    @Select("SELECT id, seller_login_id, buyer_login_id, product_id, buy_quantity, used_point, total_price, refunded_price, payment_tx_id, status " +
            "FROM orders " +
            "WHERE id = #{id}")
    OrderEntity findById(final String id);
}
