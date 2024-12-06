package com.flab.just_10_minutes.common.convertor.slack.model;

import com.flab.just_10_minutes.common.convertor.slack.color.Color;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Component
public class SlackClient {

    @Value("${webhook.slack.url}")
    private String URL;

    private final Slack slackClient;

    public SlackClient() {
        slackClient = Slack.getInstance();
    }

    public void sendMessage(SlackMessage slackMessage) {
        try {
            slackClient.send(URL, payload(p -> p.text(slackMessage.getTitle())
                                                    .attachments(List.of(Attachment.builder().color(Color.RED.getCode())
                                                    .fields(slackMessage.getData().keySet().stream()
                                                                        .map(key -> generateSlackField(key, slackMessage.getData().get(key)))
                                                                        .collect(Collectors.toList())
                                                            ).build()))));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
