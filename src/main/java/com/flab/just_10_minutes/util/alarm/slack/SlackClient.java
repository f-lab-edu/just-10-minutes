package com.flab.just_10_minutes.util.alarm.slack;

import com.flab.just_10_minutes.util.alarm.color.Color;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Component
public class SlackClient {

    @Value("${webhook.slack.url}")
    private String URL;

    private static final Slack slackClient = Slack.getInstance();

    public void sendMessage(String title, Map<String, String> data) {
        try {
            slackClient.send(URL, payload(p -> p.text(title)
                                                    .attachments(List.of(Attachment.builder().color(Color.RED.getCode())
                                                    .fields(data.keySet().stream()
                                                                        .map(key -> generateSlackField(key, data.get(key)))
                                                                        .collect(Collectors.toList())
                                                            ).build()))));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
