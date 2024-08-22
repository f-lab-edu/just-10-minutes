package com.flab.just_10_minutes.Payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamportPayment {

    @JsonProperty("imp_uid")
    private String impUid;
    @JsonProperty("merchant_uid")
    private String merchantUid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("buyer_name")
    private String buyerName;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("paid_at")
    private Date paidAt;
    @JsonProperty("pay_method")
    private String payMethod;
    @JsonProperty("status")
    private String status;
    @JsonProperty("cancel_amount")
    private BigDecimal cancelAmount;
    @JsonProperty("cancel_reason")
    private String cancelReason;
    @JsonProperty("cancelled_at")
    private Date cancelledAt;
    @JsonProperty("fail_reason")
    private String failReason;
    @JsonProperty("failed_at")
    private Date failedAt;
}
