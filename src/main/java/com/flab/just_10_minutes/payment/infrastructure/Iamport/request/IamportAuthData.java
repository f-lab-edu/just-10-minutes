package com.flab.just_10_minutes.payment.infrastructure.Iamport.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportAuthData {

    private String impKey;
    private String impSecret;
}
