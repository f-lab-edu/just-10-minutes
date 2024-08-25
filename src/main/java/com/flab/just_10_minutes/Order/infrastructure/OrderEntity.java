package com.flab.just_10_minutes.Order.infrastructure;

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
    //TODO(due : 8/17): ProductId;
    private Long usedPoint;
    private Long buyQuantity;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private String status;

    public static OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .sellerLoginId(order.getSeller().getLoginId())
                .buyerLoginId(order.getBuyer().getLoginId())
                .usedPoint(order.getUsedPoint())
                .buyQuantity(order.getBuyQuantity())
                .totalPrice(order.getTotalPrice())
                .refundedPrice(order.getRefundedPrice())
                .paymentTxId(order.getPaymentTxId())
                .status(order.getStatus().toString())
                .build();
    }
}
