package com.flab.just_10_minutes.Product.service;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.infrastructure.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockManager {

    private final ProductDao productDao;

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
