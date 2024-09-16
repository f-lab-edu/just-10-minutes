package com.flab.just_10_minutes.payment.dto;

import com.flab.just_10_minutes.payment.infrastructure.Iamport.request.IamportBillingCustomerData;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BillingRequest {

    @NotEmpty
    private PG pg;
    @NotEmpty
    private String cardNumber;
    @NotEmpty
    private String expiry;
    @NotEmpty
    private String birth;
    @NotEmpty
    private String pwd2Digit;

    public static IamportBillingCustomerData toIamportBillingCustomerData(BillingRequest billingRequest, @NonNull String pg) {
        return IamportBillingCustomerData.builder()
                .pg(pg)
                .cardNumber(billingRequest.getCardNumber())
                .expiry(billingRequest.getExpiry())
                .birth(billingRequest.getBirth())
                .pwd_2digit(billingRequest.getPwd2Digit())
                .build();
    }

    public enum PG {
        NICE,
    }
}
