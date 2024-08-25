package com.flab.just_10_minutes.Payment.dto;

import com.flab.just_10_minutes.Payment.infrastructure.Iamport.request.IamportAgainPaymentData;
import com.flab.just_10_minutes.User.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentRequest {

    @NotEmpty
    private String merchantUid;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private String customerLoginId;
    @NotNull
    private String orderName;
    @NotNull
    private BillingRequest billingRequest;

    public static IamportAgainPaymentData toAgainPaymentData(PaymentRequest paymentRequest, final String customerUid) {
        return IamportAgainPaymentData.builder()
                                .customerUid(customerUid)
                                .merchantUid(paymentRequest.merchantUid)
                                .amount(paymentRequest.getTotalPrice())
                                .name(paymentRequest.getOrderName())
                                .buyerName(paymentRequest.getCustomerLoginId())
                                .build();
    }

    public static PaymentRequest from (String orderId, Long totalPrice, User buyer, Long productId, BillingRequest billingRequest) {
        return PaymentRequest.builder()
                .merchantUid(orderId)
                .totalPrice(BigDecimal.valueOf(totalPrice))
                .customerLoginId(buyer.getLoginId())
                .orderName(productId.toString())
                .billingRequest(billingRequest)
                .build();
    }
}
