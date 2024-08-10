package com.flab.just_10_minutes.Order.controller;

import com.flab.just_10_minutes.Order.dto.OrderDto;
import com.flab.just_10_minutes.Order.dto.OrderReceipt;
import com.flab.just_10_minutes.Order.service.OrderService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderReceipt> order(@RequestBody OrderDto orderDto) throws IamportResponseException, IOException {
        return ResponseEntity.ok(orderService.order(orderDto));
    }
}
