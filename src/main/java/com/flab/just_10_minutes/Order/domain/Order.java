package com.flab.just_10_minutes.Order.domain;

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

    private String orderId;
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
}
