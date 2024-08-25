package com.flab.just_10_minutes.Payment.infrastructure.Iamport.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportAgainPaymentData {

    private String customerUid;
    private String merchantUid;
    private BigDecimal amount;
    private String name;
    private String buyerName;
}
