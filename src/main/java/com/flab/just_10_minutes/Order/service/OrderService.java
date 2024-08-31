package com.flab.just_10_minutes.Order.service;

import com.flab.just_10_minutes.Order.domain.Order;
import com.flab.just_10_minutes.Order.dto.OrderDto;
import com.flab.just_10_minutes.Order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.Order.infrastructure.repository.OrderDao;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.dto.PaymentRequest;
import com.flab.just_10_minutes.Payment.gateway.PaymentService;
import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.service.PointService;
import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.Product.service.StockService;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.flab.just_10_minutes.Util.Common.IDUtil.issueOrderId;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    private final PointService pointService;
    private final PaymentService paymentService;
    private final StockService stockService;

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
