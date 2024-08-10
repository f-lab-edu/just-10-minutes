package com.flab.just_10_minutes.Order.service;

import com.flab.just_10_minutes.Order.domain.Order;
import com.flab.just_10_minutes.Order.dto.OrderDto;
import com.flab.just_10_minutes.Order.dto.OrderReceipt;
import com.flab.just_10_minutes.Order.infrastructure.OrderDao;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.dto.PaymentDataDto;
import com.flab.just_10_minutes.Payment.gateway.PaymentGateWay;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final PaymentGateWay paymentGateWay;
    //TODO(due : 8/17): ProductDao

    public OrderReceipt order(OrderDto orderDto) {
        User seller = userDao.fetch(orderDto.getSellerLoginId());
        User buyer = userDao.fetch(orderDto.getBuyerLoginId());

        /*
        * TODO(due : 8/17): 재고 차감
        *  - 상품 재고 락 걸기
        *  - 재고 차감 가능 여부 검증
        *  - 재고 차감
        * */
        Long productOriginalPrice = 10000L;

        /*
        * TODO(due : 8/19): 포인트 차감, 최종 가격 계산
        * */
        Long totalPrice = orderDto.getRequestDecreasedStock() * productOriginalPrice;

        PaymentResult paymentResult = paymentGateWay.paymentTransaction(PaymentDataDto.builder()
                                                                                        .orderId(UUID.randomUUID().toString())
                                                                                        .amount(BigDecimal.valueOf(totalPrice))
                                                                                        .customerLoginId(buyer.getLoginId())
                                                                                        .orderName(orderDto.getProductId().toString())
                                                                                        .billingData(orderDto.getBillingData())
                                                                                        .build());

        Order order = Order.builder()
                            .orderId(paymentResult.getOrderId())
                            .seller(seller)
                            .buyer(buyer)
                            .usedPoint(0L)
                            .buyQuantity(orderDto.getRequestDecreasedStock())
                            .totalPrice(totalPrice)
                            .refundedPrice(0L)
                            .paymentTxId(paymentResult.getPaymentTxId())
                            .status(Order.OrderStatus.COMPLETED)
                            .build();

        orderDao.save(order);
        return showReceipt(order.getOrderId());
    }

    public OrderReceipt showReceipt(final String orderId) {
        return orderDao.fetch(orderId);
    }
}
