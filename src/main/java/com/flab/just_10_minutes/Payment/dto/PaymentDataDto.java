package com.flab.just_10_minutes.Payment.dto;

import com.flab.just_10_minutes.Payment.domain.BillingData;
import com.siot.IamportRestClient.request.AgainPaymentData;
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
public class PaymentDataDto {

    @NotEmpty
    private String orderId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String customerLoginId;
    @NotNull
    private String orderName;
    @NotNull
    private BillingData billingData;

    public static AgainPaymentData toPaymentData(PaymentDataDto paymentData, final String billingKey) {
        AgainPaymentData data = new AgainPaymentData(billingKey, paymentData.getOrderId(), paymentData.getAmount());
        data.setName(paymentData.getOrderName());
        data.setBuyerName(paymentData.getCustomerLoginId());
        return data;
    }
}
