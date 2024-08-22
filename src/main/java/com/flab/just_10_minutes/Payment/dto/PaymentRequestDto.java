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
public class PaymentRequestDto {

    @NotEmpty
    private String merchantUid;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private String customerLoginId;
    @NotNull
    private String orderName;
    @NotNull
    private BillingRequestDto billingRequestDto;

    public static IamportAgainPaymentData toAgainPaymentData(PaymentRequestDto paymentRequestDto, final String customerUid) {
        return IamportAgainPaymentData.builder()
                                .customerUid(customerUid)
                                .merchantUid(paymentRequestDto.merchantUid)
                                .amount(paymentRequestDto.getTotalPrice())
                                .name(paymentRequestDto.getOrderName())
                                .buyerName(paymentRequestDto.getCustomerLoginId())
                                .build();
    }

    public static PaymentRequestDto from (String orderId, Long totalPrice, User buyer, Long productId, BillingRequestDto billingRequestDto) {
        return PaymentRequestDto.builder()
                .merchantUid(orderId)
                .totalPrice(BigDecimal.valueOf(totalPrice))
                .customerLoginId(buyer.getLoginId())
                .orderName(productId.toString())
                .billingRequestDto(billingRequestDto)
                .build();
    }
}
