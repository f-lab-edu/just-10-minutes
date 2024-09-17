package com.flab.just_10_minutes.user.domain;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Customer {

    private String loginId;
    private String phone;
    private String address;
    private User.ROLE role;

    public static Customer from(User user) {
        return Customer.builder()
                .loginId(user.getLoginId())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }
}