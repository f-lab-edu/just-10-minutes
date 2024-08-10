package com.flab.just_10_minutes.Order.dto;

import com.flab.just_10_minutes.Payment.domain.BillingData;
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
    private Long requestUsedPoint;
    @NotNull
    private BillingData billingData;
}
