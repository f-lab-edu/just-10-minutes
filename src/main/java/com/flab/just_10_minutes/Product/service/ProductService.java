package com.flab.just_10_minutes.Product.service;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.dto.ProductDto;
import com.flab.just_10_minutes.Product.infrastructure.ProductDao;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;
    private final UserDao userDao;

    public Product save(ProductDto productDto) {
        User seller = userDao.fetch(productDto.getSellerId());

        Product product = ProductDto.toDomain(productDto, seller);
        return productDao.save(product);
    }

    @Transactional
    public Product decreaseStock(final Long productId, final Long requestQuantity) {
        Product product = productDao.fetchWithLock(productId);
        Long updatedStock = Math.abs(requestQuantity) + product.getPurchasedStock();
        if (updatedStock > product.getTotalStock()) {
            throw new RuntimeException("over totalStock");
        }
        return productDao.patchStock(productId, updatedStock);
    }
}
