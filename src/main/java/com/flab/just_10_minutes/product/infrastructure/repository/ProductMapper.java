package com.flab.just_10_minutes.product.infrastructure.repository;

import com.flab.just_10_minutes.product.infrastructure.entity.ProductEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO products (title, description, seller_id, original_price, total_stock, purchased_stock) " +
            "VALUES (#{title}, #{description}, #{sellerId}, #{originalPrice}, #{totalStock}, #{purchasedStock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(ProductEntity productEntity);

    @Select("SELECT * FROM products WHERE id = #{id}")
    ProductEntity findById(final Long id);

    @Select("SELECT * FROM products WHERE id = #{id} FOR UPDATE")
    ProductEntity findByIdForUpdate(final Long id);

    @Update("UPDATE products SET purchased_stock = purchased_stock + #{requestQuantity} " +
            "WHERE id = #{id} " +
            "AND purchased_stock + #{requestQuantity} >= total_stock")
    int patchStock(final Long id, final Long requestQuantity);

    @Delete("DELETE FROM products WHERE id = #{id}")
    int delete(final Long id);
}
