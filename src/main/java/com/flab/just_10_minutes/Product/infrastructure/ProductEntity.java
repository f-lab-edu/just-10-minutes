package com.flab.just_10_minutes.Product.infrastructure;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class ProductEntity {

    private Long id;
    private String title;
    private String description;
    private String sellerId;
    private Long originalPrice;
    private Long totalStock;
    private Long purchasedStock;

    public static ProductEntity toEntity(Product product) {
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
                .seller(Product.Seller.toSeller(seller))
                .originalPrice(productEntity.getOriginalPrice())
                .totalStock(productEntity.getTotalStock())
                .purchasedStock(productEntity.getPurchasedStock())
                .build();
    }
}
