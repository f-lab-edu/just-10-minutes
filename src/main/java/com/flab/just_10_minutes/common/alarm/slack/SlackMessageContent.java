package com.flab.just_10_minutes.common.alarm.slack;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SlackMessageContent {

    private final Map<String, String> fields;

    private SlackMessageContent(Map<String, String> fields) {
        this.fields = new HashMap<>(fields);
    }

    public static Builder builder() {
        return new SlackMessageContent.Builder();
    }

    public Map<String, String> getAllFields() {
        return new HashMap<>(this.fields);
    }

    public static class Builder {
        private final Map<String, String> fields = new HashMap<>();

        public Builder addField(final String key, final String field) {
            fields.putIfAbsent(key, field);
            return this;
        }

        public SlackMessageContent build() {
            return new SlackMessageContent(fields);
        }
    }
}
