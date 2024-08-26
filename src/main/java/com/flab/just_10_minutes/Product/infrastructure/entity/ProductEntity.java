package com.flab.just_10_minutes.Product.infrastructure.entity;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.User.domain.Customer;
import com.flab.just_10_minutes.User.domain.User;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ProductEntity {

    private Long id;
    private String title;
    private String description;
    private String sellerId;
    private Long originalPrice;
    private Long totalStock;
    private Long purchasedStock;

    public static ProductEntity from(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .sellerId(product.getSeller().getLoginId())
                .originalPrice(product.getOriginalPrice())
                .totalStock(product.getTotalStock())
                .purchasedStock(product.getPurchasedStock())
                .build();
    }

    public static Product toDomain(ProductEntity productEntity, User seller) {
        return Product.builder()
                .id(productEntity.getId())
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .seller(Customer.from(seller))
                .originalPrice(productEntity.getOriginalPrice())
                .totalStock(productEntity.getTotalStock())
                .purchasedStock(productEntity.getPurchasedStock())
                .build();
    }
}
