package com.flab.just_10_minutes.Util.Config;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class IamportConfig {

    @Value("${payment.iamport.imp_key}")
    private String impKey;

    @Value("${payment.iamport.imp_secret}")
    private String impSecret;

    public static IamportClient iamportClient;

    @PostConstruct
    public void init() {
        iamportClient = new IamportClient(impKey, impSecret);
    }

    public static final String NICE_PG = "nice_v2.iamport01m";
}
