package com.flab.just_10_minutes.Payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportBillingCustomer {

    private String customerUid;
    private String pgProvider;
    private String pgId;
    private String cardName;
    private String cardCode;
    private String cardNumber;
    private String cardType;
}
