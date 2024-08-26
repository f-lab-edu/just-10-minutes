package com.flab.just_10_minutes.Product.infrastructure;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_UPDATE;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.*;

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
        User seller = Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId())).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, USER);});

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product fetchWithPessimisticLock(final Long id) {
        ProductEntity productEntity = findByIdForUpdate(id).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, PRODUCT);});
        User seller = Optional.ofNullable(userMapper.findByLoginId(productEntity.getSellerId())).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND, USER);});

        return ProductEntity.toDomain(productEntity, seller);
    }

    public Product patchStock(final Long id, final Long updatedStock) {
        int patchResult = productMapper.patchStock(id, updatedStock);

        if (patchResult != 1) {
            throw new InternalException(FAIL_TO_UPDATE);
        }
        return fetch(id);
    }

    public void delete(final Long productId) {
        productMapper.delete(productId);
    }
}
