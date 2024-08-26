package com.flab.just_10_minutes.Product.service;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.infrastructure.ProductDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductDao productDao;

    @Transactional
    public Product decreaseStock(final Long productId, final Long requestQuantity) {
        Product product = productDao.fetchWithPessimisticLock(productId);
        Long updatedStock = Math.abs(requestQuantity) + product.getPurchasedStock();

        if (updatedStock > product.getTotalStock()) {
            throw new BusinessException("Exceed Total Stock");
        }
        return productDao.patchStock(productId, updatedStock);
    }
}
