package com.flab.just_10_minutes.Product.service;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductDao productDao;

    @Transactional(timeout = 100)
    public Product decreaseStock(final Long productId, final Long requestQuantity) {
        log.info("[Thread id] " + Thread.currentThread().getId() + " Transaction Start");
        Product product = productDao.fetchWithPessimisticLock(productId);
        log.info("[Product id] " + product.getId() + " Current Stock : " + product.getPurchasedStock());
        Long updatedStock = Math.abs(requestQuantity) + product.getPurchasedStock();

        if (updatedStock > product.getTotalStock()) {
            throw new BusinessException("Exceed Total Stock");
        }
        Product newProduct = productDao.patchStock(productId, updatedStock);
        log.info("[Product id]" + newProduct.getId() + " CurrentStock :" + newProduct.getPurchasedStock());
        log.info("[Thread id] " + Thread.currentThread().getId() + " Transaction End");
        return newProduct;
    }
}
