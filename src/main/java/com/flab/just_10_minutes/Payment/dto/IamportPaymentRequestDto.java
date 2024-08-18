package com.flab.just_10_minutes.Payment.dto;

import com.flab.just_10_minutes.User.domain.User;
import com.siot.IamportRestClient.request.AgainPaymentData;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import static com.flab.just_10_minutes.Util.Common.IDUtil.issueOrderId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IamportPaymentRequestDto {

    @NotEmpty
    private String orderId;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private String customerLoginId;
    @NotNull
    private String orderName;
    @NotNull
    private BillingRequestDto billingRequestDto;

    public static AgainPaymentData toAgainPaymentData(IamportPaymentRequestDto iamportPaymentRequestDto, final String customerUid) {
        AgainPaymentData data = new AgainPaymentData(customerUid, iamportPaymentRequestDto.getOrderId(), iamportPaymentRequestDto.getTotalPrice());
        data.setName(iamportPaymentRequestDto.getOrderName());
        data.setBuyerName(iamportPaymentRequestDto.getCustomerLoginId());
        return data;
    }

    public static IamportPaymentRequestDto from (Long totalPrice, User buyer, Long productId, BillingRequestDto billingRequestDto) {
        return IamportPaymentRequestDto.builder()
                .orderId(issueOrderId())
                .totalPrice(BigDecimal.valueOf(totalPrice))
                .customerLoginId(buyer.getLoginId())
                .orderName(productId.toString())
                .billingRequestDto(billingRequestDto)
                .build();
    }
}
