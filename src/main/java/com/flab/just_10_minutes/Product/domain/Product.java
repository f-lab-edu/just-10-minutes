package com.flab.just_10_minutes.Product.domain;

import com.flab.just_10_minutes.User.domain.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Product {

    private Long id;
    private String title;
    private String description;
    private Seller seller;
    private Long originalPrice;
    private Long totalStock;
    private Long purchasedStock;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @EqualsAndHashCode
    public static class Seller {

        private String loginId;
        private String phone;
        private String address;
        private User.ROLE role;

        public static Seller toSeller(User user) {
            return Product.Seller.builder()
                    .loginId(user.getLoginId())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .build();
        }
    }
}
