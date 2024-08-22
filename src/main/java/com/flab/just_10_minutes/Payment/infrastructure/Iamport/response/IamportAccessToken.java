package com.flab.just_10_minutes.Payment.infrastructure.Iamport.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IamportAccessToken {

        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expired_at")
        private int expiredAt;
        @JsonProperty("now")
        private int now;
}
