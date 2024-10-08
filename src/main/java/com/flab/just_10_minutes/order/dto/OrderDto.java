package com.flab.just_10_minutes.order.dto;

import com.flab.just_10_minutes.payment.dto.BillingRequest;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderDto {

    @NotEmpty
    private String sellerLoginId;
    @NotEmpty
    private String buyerLoginId;
    @NotNull
    private Long productId;
    @NotNull
    private Long requestDecreasedStock;
    @NotNull
    @NegativeOrZero
    private Long requestUsedPoint;
    @NotNull
    private BillingRequest billingRequest;
}
