package com.flab.just_10_minutes.Product.infrastructure.repository;

import com.flab.just_10_minutes.Product.infrastructure.entity.ProductEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO products (title, description, seller_id, original_price, total_stock, purchased_stock) " +
            "VALUES (#{title}, #{description}, #{sellerId}, #{originalPrice}, #{totalStock}, #{purchasedStock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int save(ProductEntity productEntity);

    @Select("SELECT * FROM products WHERE id = #{id}")
    public ProductEntity findById(final Long id);

    @Select("SELECT * FROM products WHERE id = #{id} FOR UPDATE")
    public ProductEntity findByIdForUpdate(final Long id);

    @Update("UPDATE products SET purchased_stock = #{updatedStock} WHERE id = #{id}")
    public int patchStock(final Long id, final Long updatedStock);

    @Delete("DELETE FROM products WHERE id = #{id}")
    public int delete(final Long id);
}