package com.flab.just_10_minutes.Util.Config;

import com.flab.just_10_minutes.Payment.dto.BillingRequestDto;
import com.siot.IamportRestClient.IamportClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class IamportConfig {

    @Value("${payment.iamport.api_key}")
    private String apiKey;

    @Value("${payment.iamport.api_secret}")
    private String apiSecret;

    @Value("${payment.iamport.nice_pg}")
    private String nicePg;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }

    public String getPg(BillingRequestDto.PG pg) {
        switch (pg) {
            case NICE -> {return getNicePg();}
            default -> {return getNicePg();}
        }
    }
}
