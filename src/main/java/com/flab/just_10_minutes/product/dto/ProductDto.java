package com.flab.just_10_minutes.product.dto;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.user.domain.Customer;
import com.flab.just_10_minutes.user.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String sellerId;
    @NotNull
    private Long originalPrice;
    @NotNull
    private Long totalStock;

    public static Product toDomain(ProductDto productDto, User seller) {
        return Product.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .seller(Customer.from(seller))
                .originalPrice(productDto.getOriginalPrice())
                .totalStock(productDto.getTotalStock())
                .purchasedStock(0L)
                .build();
    }
}
