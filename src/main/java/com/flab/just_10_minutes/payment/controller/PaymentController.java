package com.flab.just_10_minutes.payment.controller;

import com.flab.just_10_minutes.payment.dto.PortOnePaidWebhookRequest;
import com.flab.just_10_minutes.payment.service.PaymentWebhookService;
import jakarta.validation.Valid;
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

    private final PaymentWebhookService paymentWebhookService;

    @PostMapping("/webhook/paid")
    public void checkWebHook(@RequestBody @Valid PortOnePaidWebhookRequest iamportWebhookDto) {
        paymentWebhookService.validatePaidWebhook(iamportWebhookDto);
    }
}
