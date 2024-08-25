package com.flab.just_10_minutes.Order.infrastructure;

import com.flab.just_10_minutes.Order.domain.Order;
import com.flab.just_10_minutes.Order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.ORDER;

@Repository
@RequiredArgsConstructor
public class OrderDao {

    private final OrderMapper orderMapper;

    public void save(Order order) {
        int saveResult = orderMapper.save(OrderEntity.toEntity(order));
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
