package com.flab.just_10_minutes.Payment.domain;

import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentResult {

    private Long id;
    private String paymentTxId;
    private String orderId;
    private String orderName;
    private String buyerName;
    private BigDecimal amount;
    private String currency;
    private Date paidAt;
    private String payMethod;
    private String status;
    private BigDecimal cancelAmount;
    private String cancelReason;
    private Date cancelledAt;
    private String failReason;
    private Date failedAt;

    public static PaymentResult from(Payment iamportPayment) {
        return PaymentResult.builder()
                .paymentTxId(iamportPayment.getImpUid())
                .orderId(iamportPayment.getMerchantUid())
                .orderName(iamportPayment.getName())
                .buyerName(iamportPayment.getBuyerName())
                .amount(iamportPayment.getAmount())
                .currency(iamportPayment.getCurrency())
                .paidAt(iamportPayment.getPaidAt())
                .payMethod(iamportPayment.getPayMethod())
                .status(iamportPayment.getStatus())
                .cancelAmount(iamportPayment.getCancelAmount())
                .cancelReason(iamportPayment.getCancelReason())
                .cancelledAt(iamportPayment.getCancelledAt())
                .failReason(iamportPayment.getFailReason())
                .failedAt(iamportPayment.getFailedAt())
                .build();
    }
}
