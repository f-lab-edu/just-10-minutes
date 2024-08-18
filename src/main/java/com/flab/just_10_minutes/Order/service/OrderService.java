package com.flab.just_10_minutes.Order.service;

import com.flab.just_10_minutes.Order.domain.Order;
import com.flab.just_10_minutes.Order.dto.OrderDto;
import com.flab.just_10_minutes.Order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.Order.infrastructure.OrderDao;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.dto.IamportPaymentRequestDto;
import com.flab.just_10_minutes.Payment.gateway.PaymentService;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final PaymentService paymentGateWay;
    //TODO(due : 8/17): ProductDao

    public OrderReceiptDto order(OrderDto orderDto) {
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

        PaymentResult paymentResult = paymentGateWay.paymentTransaction(IamportPaymentRequestDto.from(totalPrice, buyer, orderDto.getRequestUsedPoint(), orderDto.getBillingRequestDto()));

        Order order = Order.from(paymentResult, seller, orderDto.getRequestDecreasedStock(), buyer, totalPrice);

        orderDao.save(order);
        return showReceipt(order.getId());
    }

    public OrderReceiptDto showReceipt(final String orderId) {
        return orderDao.fetch(orderId);
    }
}
