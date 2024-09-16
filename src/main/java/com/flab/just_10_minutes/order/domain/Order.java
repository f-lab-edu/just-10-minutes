package com.flab.just_10_minutes.order.domain;

import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.point.domain.PointHistory;
import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.user.domain.User;
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
    private Product product;
    private Long buyQuantity;
    private Long usedPoint;
    private Long totalPrice;
    private Long refundedPrice;
    private String paymentTxId;
    private OrderStatus status;

    public enum OrderStatus {
        COMPLETED,
        REFUNDED
    }

    public static Order createCompleteOrder(PaymentResult paymentResult,
                                            User seller,
                                            User buyer,
                                            Product product,
                                            Long buyQuantity,
                                            PointHistory pointHistory,
                                            Long totalPrice) {
        return Order.builder()
                .id(paymentResult.getMerchantUid())
                .seller(seller)
                .buyer(buyer)
                .product(product)
                .buyQuantity(buyQuantity)
                .usedPoint(pointHistory.getRequestedQuantity())
                .totalPrice(totalPrice)
                .refundedPrice(0L)
                .paymentTxId(paymentResult.getImpUid())
                .status(Order.OrderStatus.COMPLETED)
                .build();
    }
}
