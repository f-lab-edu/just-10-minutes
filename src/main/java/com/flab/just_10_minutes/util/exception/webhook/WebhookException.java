package com.flab.just_10_minutes.util.exception.webhook;

import com.flab.just_10_minutes.util.alarm.slack.SlackMessage;
import lombok.Getter;

import static com.flab.just_10_minutes.util.exception.webhook.WebHookMessage.LOGGING_MESSAGE_PREFIX;

@Getter
public class WebhookException extends RuntimeException {

  private SlackMessage slackMessage;

  public WebhookException(String message, SlackMessage slackMessage) {
    super(LOGGING_MESSAGE_PREFIX + message);
    this.slackMessage = slackMessage;
  }
}
