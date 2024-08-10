package com.flab.just_10_minutes.Payment.domain;

import com.siot.IamportRestClient.request.BillingCustomerData;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BillingData {

    @NotEmpty
    private String pg;
    @NotEmpty
    private String cardNumber;
    @NotEmpty
    private String expiry;
    @NotEmpty
    private String birth;
    @NotEmpty
    private String pwd_2Digit;

    public static BillingCustomerData toBillingCustomerData(BillingData billingKeyDto) {
        BillingCustomerData data =  new BillingCustomerData(null,
                billingKeyDto.getCardNumber(),
                billingKeyDto.getExpiry(),
                billingKeyDto.getBirth());
        data.setPg(billingKeyDto.getPg());
        data.setPwd2Digit(billingKeyDto.getPwd_2Digit());
        return data;
    }
}
