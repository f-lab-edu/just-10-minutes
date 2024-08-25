package com.flab.just_10_minutes.Payment.infrastructure.Iamport.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportBillingCustomerData {

    private String pg;
    private String cardNumber;
    private String expiry;
    private String birth;
    private String pwd_2digit;
    private String cvc;
}
