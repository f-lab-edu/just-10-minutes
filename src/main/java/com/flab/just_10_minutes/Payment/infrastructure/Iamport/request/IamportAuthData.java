package com.flab.just_10_minutes.Payment.infrastructure.Iamport.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IamportAuthData {

    @JsonProperty("imp_key")
    private String impKey;
    @JsonProperty("imp_secret")
    private String impSecret;
}
