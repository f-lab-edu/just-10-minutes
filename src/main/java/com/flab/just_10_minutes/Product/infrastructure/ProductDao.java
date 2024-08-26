package com.flab.just_10_minutes.Product.infrastructure;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductDao {

    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    public Product save(Product product) {
        ProductEntity productEntity = ProductEntity.from(product);

        int saveResult = productMapper.save(productEntity);
        if (saveResult != 1) {
            throw new RuntimeException("Fail to Insert");
        }

        return fetch(productEntity.getId());
    }

    private Optional<ProductEntity> findById(final Long id) {
        return Optional.ofNullable(productMapper.findById(id));
    }

    private Optional<ProductEntity> findByIdForUpdate(final Long id) {
        return Optional.ofNullable(productMapper.findByIdForUpdate(id));
    }

    public Product fetch(final Long id) {
        ProductEntity productEntity = findById(id).orElseThrow(() -> {throw new RuntimeException("Not Exist Product");});
        User seller = Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId())).orElseThrow(() -> {throw new RuntimeException("Not Exist User");});

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product fetchWithPessimisticLock(final Long id) {
        ProductEntity productEntity = findByIdForUpdate(id).orElseThrow(() -> {throw new RuntimeException("Not Exist Product");});
        User seller = Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId())).orElseThrow(() -> {throw new RuntimeException("Not Exist User");});

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product patchStock(final Long id, final Long updatedStock) {
        int patchResult = productMapper.patchStock(id, updatedStock);

        if (patchResult != 1) {
            throw new RuntimeException("Fail to Update");
        }
        return fetch(id);
    }

    public void delete(final Long productId) {
        productMapper.delete(productId);
    }
}
