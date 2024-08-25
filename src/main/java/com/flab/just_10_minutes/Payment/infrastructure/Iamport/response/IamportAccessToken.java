package com.flab.just_10_minutes.Payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportAccessToken {

        private String accessToken;
        private int expiredAt;
        private int now;
}
