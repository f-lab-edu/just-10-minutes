package com.flab.just_10_minutes.Payment.infrastructure.Iamport.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IamportBillingCustomerData {

    @JsonProperty("pg")
    private String pg;
    @JsonProperty("card_number")
    private String cardNumber;
    @JsonProperty("expiry")
    private String expiry;
    @JsonProperty("birth")
    private String birth;
    @JsonProperty("pwd_2digit")
    private String pwd2digit;
    @JsonProperty("cvc")
    private String cvc;
}
