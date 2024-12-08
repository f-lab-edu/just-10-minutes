package com.flab.just_10_minutes.order.service;

import com.flab.just_10_minutes.order.domain.Order;
import com.flab.just_10_minutes.order.dto.OrderDto;
import com.flab.just_10_minutes.order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.order.infrastructure.repository.OrderDao;
import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.payment.dto.PaymentRequest;
import com.flab.just_10_minutes.payment.service.PaymentService;
import com.flab.just_10_minutes.point.domain.PointHistory;
import com.flab.just_10_minutes.point.service.PointService;
import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.product.service.StockService;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.common.util.IDUtil.issueOrderId;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    private final PointService pointService;
    private final PaymentService paymentService;
    private final StockService stockService;

    @Transactional
    public OrderReceiptDto order(OrderDto orderDto) {
        User seller = userDao.fetch(orderDto.getSellerLoginId());
        User buyer = userDao.fetch(orderDto.getBuyerLoginId());
        String orderId = issueOrderId();

        Product product = productDao.fetch(orderDto.getProductId());
        stockService.decreaseStock(product.getId(), orderDto.getRequestDecreasedStock());

        PointHistory pointHistory = PointHistory.from(buyer,
                                                    orderDto.getRequestUsedPoint(),
                                            "Order:" + orderId);

        PointHistory newHistory = pointService.subtractPoint(pointHistory);

        long totalPrice = product.calculateTotalPrice(orderDto.getRequestDecreasedStock(), newHistory.getRequestedQuantity());
        PaymentResult paymentResult = paymentService.paymentTransaction(PaymentRequest.from(orderId,
                                                                                            totalPrice,
                                                                                            buyer,
                                                                                            orderDto.getRequestUsedPoint(),
                                                                                            orderDto.getBillingRequest()));

        Order order = Order.createCompleteOrder(paymentResult,
                                                seller,
                                                buyer,
                                                product,
                                                orderDto.getRequestDecreasedStock(),
                                                newHistory,
                                                totalPrice);

        orderDao.save(order);
        return showReceipt(order.getId());
    }

    public OrderReceiptDto showReceipt(final String orderId) {
        return orderDao.fetch(orderId);
    }
}
