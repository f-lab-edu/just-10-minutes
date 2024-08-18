package com.flab.just_10_minutes.Order.dto;

import com.flab.just_10_minutes.Order.infrastructure.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class OrderReceiptDto {

    private String orderId;
    private String sellerLoginId;
    private String buyerLoginId;
    //TODO(due : 8/17) : private Long productId;
    //TODO(due : 8/17): private ProductReceipt productReceipt;
    private Long usedPoint;
    private Long buyQuantity;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private String status;

    public static OrderReceiptDto from(OrderEntity orderEntity) {
        return OrderReceiptDto.builder()
                .orderId(orderEntity.getId())
                .sellerLoginId(orderEntity.getSellerLoginId())
                .buyerLoginId(orderEntity.getBuyerLoginId())
                .usedPoint(orderEntity.getUsedPoint())
                .buyQuantity(orderEntity.getBuyQuantity())
                .totalPrice(orderEntity.getTotalPrice())
                .refundedPrice(orderEntity.getRefundedPrice())
                .paymentTxId(orderEntity.getPaymentTxId())
                .status(orderEntity.getStatus())
                .build();
    }
}
