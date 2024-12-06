package com.flab.just_10_minutes.order.infrastructure.repository;

import com.flab.just_10_minutes.order.domain.Order;
import com.flab.just_10_minutes.order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.order.infrastructure.entity.OrderEntity;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.common.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.ORDER;

@Repository
@RequiredArgsConstructor
public class OrderDao {

    private final OrderMapper orderMapper;

    public void save(Order order) {
        int saveResult = orderMapper.save(OrderEntity.from(order));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    public Optional<OrderEntity> findById(final String id) {
        return Optional.ofNullable(orderMapper.findById(id));
    }

    public OrderReceiptDto fetch(final String id) {
        OrderEntity orderEntity = findById(id).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, ORDER);
        });
        return OrderReceiptDto.from(orderEntity);
    }
}
