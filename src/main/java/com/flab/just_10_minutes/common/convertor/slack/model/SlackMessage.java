package com.flab.just_10_minutes.common.convertor.slack.model;

import com.flab.just_10_minutes.payment.domain.PaymentResultStatus;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.PAID;

@Getter
@Builder
public class SlackMessage {

    private String title;
    private Map<String, String> data;

    public static SlackMessage createImpUidMissingInternal(String impUid) {
        return SlackMessage.builder()
                .title("결제 이상 발생 : 결제 건 미존재")
                .data(SlackMessageContent.builder()
                                            .addField("결제 ID",impUid)
                                            .build()
                                            .getAllFields())
                .build();
    }

    public static SlackMessage createDiffersStatus(String impUid, PaymentResultStatus internalStatus) {
        return SlackMessage.builder()
                .title("결제 이상 발생 : 결제 상태 불일치")
                .data(SlackMessageContent.builder()
                                            .addField("서비스에 저장된 결제 상태", internalStatus.getLable())
                                            .addField("포트원 결제 상태", PAID.getLable())
                                            .addField("결제 ID", impUid)
                                            .build()
                                            .getAllFields())
                .build();
    }
}
