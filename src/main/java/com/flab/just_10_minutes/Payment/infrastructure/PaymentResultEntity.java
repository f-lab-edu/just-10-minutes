package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.domain.PaymentResultStatus;
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
public class PaymentResultEntity {

    private Long id;
    private String impUid;
    private String merchantUid;
    private String name;
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

    public static PaymentResultEntity from(PaymentResult paymentResult) {
        return PaymentResultEntity.builder()
                .impUid(paymentResult.getImpUid())
                .merchantUid(paymentResult.getMerchantUid())
                .name(paymentResult.getName())
                .buyerName(paymentResult.getBuyerName())
                .amount(paymentResult.getAmount())
                .currency(paymentResult.getCurrency())
                .paidAt(paymentResult.getPaidAt())
                .payMethod(paymentResult.getPayMethod())
                .status(paymentResult.getStatus().getLable())
                .cancelAmount(paymentResult.getCancelAmount())
                .cancelReason(paymentResult.getCancelReason())
                .cancelledAt(paymentResult.getCancelledAt())
                .failReason(paymentResult.getFailReason())
                .failedAt(paymentResult.getFailedAt())
                .build();
    }

    public static PaymentResult to(PaymentResultEntity paymentResultEntity) {
        return PaymentResult.builder()
                .impUid(paymentResultEntity.getImpUid())
                .merchantUid(paymentResultEntity.getMerchantUid())
                .name(paymentResultEntity.getName())
                .buyerName(paymentResultEntity.getBuyerName())
                .amount(paymentResultEntity.getAmount())
                .currency(paymentResultEntity.getCurrency())
                .paidAt(paymentResultEntity.getPaidAt())
                .payMethod(paymentResultEntity.getPayMethod())
                .status(PaymentResultStatus.from(paymentResultEntity.getStatus()))
                .cancelAmount(paymentResultEntity.getCancelAmount())
                .cancelReason(paymentResultEntity.getCancelReason())
                .cancelledAt(paymentResultEntity.getCancelledAt())
                .failReason(paymentResultEntity.getFailReason())
                .failedAt(paymentResultEntity.getFailedAt())
                .build();
    }
}
