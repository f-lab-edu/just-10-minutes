package com.flab.just_10_minutes.message.fcm.service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PublishMessageEvent {

    private String messageId;
}
