package com.flab.just_10_minutes.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flab.just_10_minutes.payment.domain.PaymentResultStatus;
import com.flab.just_10_minutes.util.validator.PaidWebhook;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PortOnePaidWebhookRequest {

    @NotEmpty
    private String impUid;

    @NotEmpty
    private String merchantUid;

    @PaidWebhook
    private PaymentResultStatus status;

    private String cancellationId;
}
