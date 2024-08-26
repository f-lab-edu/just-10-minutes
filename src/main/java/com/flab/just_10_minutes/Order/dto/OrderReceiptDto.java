package com.flab.just_10_minutes.Order.dto;

import com.flab.just_10_minutes.Order.infrastructure.entity.OrderEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OrderReceiptDto {

    private String orderId;
    private String sellerLoginId;
    private String buyerLoginId;
    private Long productId;
    private Long buyQuantity;
    private Long usedPoint;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private String status;

    public static OrderReceiptDto from(OrderEntity orderEntity) {
        return OrderReceiptDto.builder()
                .orderId(orderEntity.getId())
                .sellerLoginId(orderEntity.getSellerLoginId())
                .buyerLoginId(orderEntity.getBuyerLoginId())
                .productId(orderEntity.getProductId())
                .buyQuantity(orderEntity.getBuyQuantity())
                .usedPoint(orderEntity.getUsedPoint())
                .totalPrice(orderEntity.getTotalPrice())
                .refundedPrice(orderEntity.getRefundedPrice())
                .paymentTxId(orderEntity.getPaymentTxId())
                .status(orderEntity.getStatus())
                .build();
    }
}
