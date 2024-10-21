package com.flab.just_10_minutes.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flab.just_10_minutes.payment.domain.PaymentResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportWebhookDto {

    private String impUid;
    private String merchantUid;
    private PaymentResultStatus status;
    private String cancellationId;
}
