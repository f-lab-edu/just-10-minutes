package com.flab.just_10_minutes.Payment.infrastructure.Iamport.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class IamportAgainPaymentData {

    @JsonProperty("customer_uid")
    private String customerUid;
    @JsonProperty("merchant_uid")
    private String merchantUid;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("name")
    private String name;
    @JsonProperty("buyer_name")
    private String buyerName;
}
