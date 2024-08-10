package com.flab.just_10_minutes.Order.infrastructure;

import com.flab.just_10_minutes.Order.domain.Order;
import com.flab.just_10_minutes.Order.dto.OrderReceipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderDao {

    private final OrderMapper orderMapper;

    public void save(Order order) {
        int saveResult = orderMapper.save(OrderEntity.toEntity(order));
        if (saveResult != 1) {
            throw new RuntimeException("INSERT FAIL");
        }
    }

    public Optional<OrderEntity> findByOrderId(final String orderId) {
        return Optional.ofNullable(orderMapper.findByOrderId(orderId));
    }

    public OrderReceipt fetch(final String orderId) {
        OrderEntity orderEntity = findByOrderId(orderId).orElseThrow(() -> {
            throw new RuntimeException("NOT EXIST");
        });
        return OrderReceipt.from(orderEntity);
    }
}
