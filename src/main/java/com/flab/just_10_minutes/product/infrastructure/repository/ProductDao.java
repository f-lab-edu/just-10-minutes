package com.flab.just_10_minutes.product.infrastructure.repository;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.infrastructure.entity.ProductEntity;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.user.infrastructure.repository.UserMapper;
import com.flab.just_10_minutes.common.exception.business.BusinessException;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.common.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.*;

@Repository
@RequiredArgsConstructor
public class ProductDao {

    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    public Product save(Product product) {
        ProductEntity productEntity = ProductEntity.from(product);

        int saveResult = productMapper.save(productEntity);
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
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
        ProductEntity productEntity = findById(id).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, PRODUCT);});
        User seller = UserEntity.toDomain(Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId()))
                                            .orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, USER);}));

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product fetchWithPessimisticLock(final Long id) {
        ProductEntity productEntity = findByIdForUpdate(id).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, PRODUCT);});
        User seller = UserEntity.toDomain(Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId()))
                                            .orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, USER);}));

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product patchStock(final Long id, final Long requestQuantity) {
        int patchResult = productMapper.patchStock(id, requestQuantity);

        if (patchResult != 1) {
            throw new BusinessException("Insufficient Stock Available");
        }
        return fetch(id);
    }

    public void delete(final Long productId) {
        productMapper.delete(productId);
    }
}
