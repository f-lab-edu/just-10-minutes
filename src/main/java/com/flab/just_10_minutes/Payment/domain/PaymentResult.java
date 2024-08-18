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
    private String impUid;
    private String merchantUid;
    private String name;
    private String buyerName;
    private BigDecimal amount;
    private String currency;
    private Date paidAt;
    private String payMethod;
    private Status status;
    private BigDecimal cancelAmount;
    private String cancelReason;
    private Date cancelledAt;
    private String failReason;
    private Date failedAt;

    public static PaymentResult from(Payment payment) {
        return PaymentResult.builder()
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .name(payment.getName())
                .buyerName(payment.getBuyerName())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paidAt(payment.getPaidAt())
                .payMethod(payment.getPayMethod())
                .status(Status.from(payment.getStatus()))
                .cancelAmount(payment.getCancelAmount())
                .cancelReason(payment.getCancelReason())
                .cancelledAt(payment.getCancelledAt())
                .failReason(payment.getFailReason())
                .failedAt(payment.getFailedAt())
                .build();
    }
}
