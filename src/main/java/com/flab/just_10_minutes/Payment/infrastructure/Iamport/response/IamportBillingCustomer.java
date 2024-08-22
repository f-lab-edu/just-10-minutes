package com.flab.just_10_minutes.Payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamportBillingCustomer {

    @JsonProperty("customer_uid")
    private String customerUid;
    @JsonProperty("pg_provider")
    private String pgProvider;
    @JsonProperty("pg_id")
    private String pgId;
    @JsonProperty("card_name")
    private String cardName;
    @JsonProperty("card_code")
    private String cardCode;
    @JsonProperty("card_number")
    private String cardNumber;
    @JsonProperty("card_type")
    private String cardType;
}
