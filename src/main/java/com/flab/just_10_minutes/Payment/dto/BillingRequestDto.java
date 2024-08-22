package com.flab.just_10_minutes.Payment.dto;

import com.flab.just_10_minutes.Payment.infrastructure.Iamport.request.IamportBillingCustomerData;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BillingRequestDto {

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

    public static IamportBillingCustomerData toIamportBillingCustomerData(BillingRequestDto billingRequestDto, @NonNull String pg) {
        return IamportBillingCustomerData.builder()
                .pg(pg)
                .cardNumber(billingRequestDto.getCardNumber())
                .expiry(billingRequestDto.getExpiry())
                .birth(billingRequestDto.getBirth())
                .pwd2digit(billingRequestDto.getPwd2Digit())
                .build();
    }

    public enum PG {
        NICE,
    }
}
