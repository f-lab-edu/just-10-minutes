package com.flab.just_10_minutes.Order.infrastructure.entity;

import com.flab.just_10_minutes.Order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class OrderEntity {

    private String id;
    private String sellerLoginId;
    private String buyerLoginId;
    private Long productId;
    private Long buyQuantity;
    private Long usedPoint;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private String status;

    public static OrderEntity from(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .sellerLoginId(order.getSeller().getLoginId())
                .buyerLoginId(order.getBuyer().getLoginId())
                .productId(order.getProduct().getId())
                .buyQuantity(order.getBuyQuantity())
                .usedPoint(order.getUsedPoint())
                .totalPrice(order.getTotalPrice())
                .refundedPrice(order.getRefundedPrice())
                .paymentTxId(order.getPaymentTxId())
                .status(order.getStatus().toString())
                .build();
    }
}
