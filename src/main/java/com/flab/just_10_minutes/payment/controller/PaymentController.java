package com.flab.just_10_minutes.payment.controller;

import com.flab.just_10_minutes.payment.dto.IamportWebhookDto;
import com.flab.just_10_minutes.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/webhook/paid")
    public void checkWebHook(@RequestBody IamportWebhookDto iamportWebhookDto) {
        paymentService.validatePaidWebhook(iamportWebhookDto);
    }
}
