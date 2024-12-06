package com.flab.just_10_minutes.common.infra.notification.fcm;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Configuration
@Getter
@Slf4j
public class FcmConfig {

    @Value("${message.fcm.base_url}")
    private String baseUrl;

    @Value("${message.fcm.message_postfix}")
    private String messagePostfix;

    @Value("${message.fcm.credential_url}")
    private String credentialUrl;

    @Value("${message.fcm.key_path}")
    private String keyPath;

    private ClassPathResource credentialClassPath;

    @PostConstruct
    public void init() throws IOException {
        credentialClassPath = new ClassPathResource(keyPath);
    }

    public InputStream getCredentialInputStream() throws IOException {
        return credentialClassPath.getInputStream();
    }

    @Bean(name = "fcmHttpClient")
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(10);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    @Bean(name = "fcmHttpClientFactory")
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClient fcmHttpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(fcmHttpClient);
        factory.setConnectionRequestTimeout(Duration.ofSeconds(30));
        factory.setConnectTimeout(Duration.ofSeconds(10));
        return factory;
    }

    @Bean(name = "fcmRestClient")
    public RestClient restClient(HttpComponentsClientHttpRequestFactory fcmHttpClientFactory) {
        return RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestFactory(fcmHttpClientFactory)
                .requestInterceptor((req, body, execution) -> {
                    log.info("Request: {}", req);
                    return execution.execute(req, body);
                })
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
