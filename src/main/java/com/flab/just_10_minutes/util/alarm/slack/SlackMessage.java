package com.flab.just_10_minutes.util.alarm.slack;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Builder
public class SlackMessage {

    private String title;
    private Map<String, String> data;

    public static SlackMessage createImpUidMissingInternal(String impUid) {
        return SlackMessage.builder()
                .title("결제 이상 발생 : 결제 건 미존재")
                .data(Stream.of(new Object[][] {
                        {"결제 ID", impUid}
                }).collect(Collectors.toMap(item -> (String) item[0], item -> (String) item[1])))
                .build();
    }

    public static SlackMessage createDiffersStatus(String impUid) {
        return SlackMessage.builder()
                .title("결제 이상 발생 : 결제 상태 불일치")
                .data(Stream.of(new Object[][] {
                        {"결제 ID", impUid}
                }).collect(Collectors.toMap(item -> (String) item[0], item -> (String) item[1])))
                .build();
    }
}
