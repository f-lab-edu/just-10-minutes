package com.flab.just_10_minutes.Payment.dto;

import com.siot.IamportRestClient.request.BillingCustomerData;
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
    private String pwd_2Digit;

    public static BillingCustomerData toBillingCustomerData(BillingRequestDto billingRequestDto, @NonNull String pg) {
        BillingCustomerData data =  new BillingCustomerData(null,
                billingRequestDto.getCardNumber(),
                billingRequestDto.getExpiry(),
                billingRequestDto.getBirth());
        data.setPg(pg);
        data.setPwd2Digit(billingRequestDto.getPwd_2Digit());
        return data;
    }

    public enum PG {
        NICE,
    }
}
