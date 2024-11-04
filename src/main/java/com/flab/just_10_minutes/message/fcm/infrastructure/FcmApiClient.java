package com.flab.just_10_minutes.message.fcm.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.infrastructure.request.FcmMessageV1Request;
import com.flab.just_10_minutes.util.message.fcm.FcmConfig;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmApiClient {

    private final FcmConfig fcmConfig;
    private final RestClient fcmRestClient;

    public void sendMessage(FcmMessage fcmMessage, FcmCampaign fcmCampaign)  {
        //TODO fcm response 에 따라 처리
        fcmRestClient.post()
                .uri(uriBuilder -> uriBuilder.path(fcmConfig.getMessagePostfix()).build())
                .header("Authorization", "Bearer " + getAccessToken())
                .body(FcmMessageV1Request.from(fcmMessage, fcmCampaign))
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<Object>() {}));
    }

    private String getAccessToken() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(fcmConfig.getKeyPath()).getInputStream())
                    .createScoped(List.of(fcmConfig.getCredentialUrl()));
            credentials.refreshIfExpired();

            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
