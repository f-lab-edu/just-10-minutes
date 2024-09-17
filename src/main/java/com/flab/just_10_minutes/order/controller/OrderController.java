package com.flab.just_10_minutes.order.controller;

import com.flab.just_10_minutes.order.dto.OrderDto;
import com.flab.just_10_minutes.order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderReceiptDto> order(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.order(orderDto));
    }
}
