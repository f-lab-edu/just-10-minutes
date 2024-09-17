package com.flab.just_10_minutes.product.domain;

import com.flab.just_10_minutes.user.domain.Customer;
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
    private Customer seller;
    private Long originalPrice;
    private Long totalStock;
    private Long purchasedStock;

    public Long calculateTotalPrice(Long requestDecreasedStock, Long requestDecreasedPrice) {
        return (this.originalPrice * requestDecreasedStock) - Math.abs(requestDecreasedPrice);
    }

}
