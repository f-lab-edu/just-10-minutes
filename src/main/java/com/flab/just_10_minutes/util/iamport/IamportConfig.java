package com.flab.just_10_minutes.util.iamport;

import com.flab.just_10_minutes.payment.dto.BillingRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Getter
@Slf4j
public class IamportConfig {

    @Value("${payment.iamport.url}")
    private String url;

    @Value("${payment.iamport.api_key}")
    private String apiKey;

    @Value("${payment.iamport.api_secret}")
    private String apiSecret;

    @Value("${payment.iamport.nice_pg}")
    private String nicePg;

    @Value("${payment.test.card_number}")
    private String cardNumber;

    @Value("${payment.test.card_expiry}")
    private String cardExpiry;

    @Value("${payment.test.card_birth}")
    private String cardBirth;

    @Value("${payment.test.card_pwd2digit}")
    private String cardPwd2Digit;


    @Bean
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(10);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                                                    .setConnectionRequestTimeout(5, TimeUnit.SECONDS)
                                                    .setResponseTimeout(3, TimeUnit.SECONDS)
                                                    .build()
                )
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectionRequestTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(3));

        return factory;
    }

    @Bean
    public RestClient restClient(HttpComponentsClientHttpRequestFactory factory) {
        return RestClient
                .builder()
                .baseUrl(url)
                .requestFactory(factory)
                .requestInterceptor((req, body, execution) -> {
                    log.info("Request: {}", req);
                    return execution.execute(req, body);
                })
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getPg(BillingRequest.PG pg) {
        switch (pg) {
            case NICE -> {return getNicePg();}
            default -> {return getNicePg();}
        }
    }
}
