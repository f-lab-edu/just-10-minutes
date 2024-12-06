package com.flab.just_10_minutes.common.infra.aws;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import java.net.URI;

@Configuration
public class AwsSQSConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String awsRegionStatic;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${spring.cloud.aws.sqs.end-point.uri}")
    private String sqsEndPointUri;

    @Bean
    public AwsCredentials awsCredentials() {
        return new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return awsAccessKey;
            }

            @Override
            public String secretAccessKey() {
                return awsSecretKey;
            }
        };
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                //TODO(~12/8): AWS SQS 생성 후 url 교체
                .endpointOverride(URI.create(sqsEndPointUri))
                .credentialsProvider(this::awsCredentials)
                .region(Region.of(awsRegionStatic))
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> sqsMessageListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .configure(builder -> {
                    //TODO(~12/8): 큐 이름이 없을 때 자동 생성 안되는 전략이 안먹히는 원인 찾기
                    builder.queueNotFoundStrategy(QueueNotFoundStrategy.FAIL);
                })
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.newTemplate(sqsAsyncClient());
    }
}
