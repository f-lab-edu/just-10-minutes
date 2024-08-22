package com.flab.just_10_minutes.Util.Iamport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.Payment.dto.BillingRequestDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import java.time.Duration;

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

    @Bean
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(10);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectionRequestTimeout(Duration.ofSeconds(30));
        factory.setConnectTimeout(Duration.ofSeconds(10));
        return factory;
    }

    @Bean
    public RestClient restClient(HttpComponentsClientHttpRequestFactory factory) {
        return RestClient
                .builder()
                .baseUrl(url)
                .requestFactory(factory)
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter(new ObjectMapper())))
                .requestInterceptor((req, body, execution) -> {
                    log.info("Request: {}", req);
                    return execution.execute(req, body);
                })
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

//    @Bean
//    public IamportClient iamportClient(RestClient restClient) {
//        return HttpServiceProxyFactory
//                .builderFor(RestClientAdapter.create(restClient))
//                .build().createClient(IamportClient.class);
//    }

    public String getPg(BillingRequestDto.PG pg) {
        switch (pg) {
            case NICE -> {return getNicePg();}
            default -> {return getNicePg();}
        }
    }
}
