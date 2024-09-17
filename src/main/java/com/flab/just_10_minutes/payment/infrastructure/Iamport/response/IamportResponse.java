package com.flab.just_10_minutes.payment.infrastructure.Iamport.response;

import lombok.Data;

@Data
public class IamportResponse<T> {

    int code;
    String message;
    T response;
}
