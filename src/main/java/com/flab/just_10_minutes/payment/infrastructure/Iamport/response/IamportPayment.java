package com.flab.just_10_minutes.payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportPayment {

    private String impUid;
    private String merchantUid;
    private String name;
    private String buyerName;
    private BigDecimal amount;
    private String currency;
    private Date paidAt;
    private String payMethod;
    private String status;
    private BigDecimal cancelAmount;
    private String cancelReason;
    private Date cancelledAt;
    private String failReason;
    private Date failedAt;
}
