package com.flab.just_10_minutes.Order.domain;

import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Order {

    private String id;
    private User seller;
    private User buyer;
    //TODO(due : 8/17): Product
    private Long usedPoint;
    private Long buyQuantity;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private OrderStatus status;

    public enum OrderStatus {
        COMPLETED,
        REFUNDED
    }

    public static Order from(PaymentResult paymentResult, User seller, Long buyQuantity, User buyer, Long totalPrice) {
        return Order.builder()
                .id(paymentResult.getMerchantUid())
                .seller(seller)
                .buyer(buyer)
                .usedPoint(0L)
                .buyQuantity(buyQuantity)
                .totalPrice(totalPrice)
                .refundedPrice(0L)
                .paymentTxId(paymentResult.getImpUid())
                .status(Order.OrderStatus.COMPLETED)
                .build();
    }
}
