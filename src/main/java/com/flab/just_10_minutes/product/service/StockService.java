package com.flab.just_10_minutes.product.service;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductDao productDao;

    @Transactional
    public Product decreaseStock(final Long productId, final Long requestQuantity) {
        return productDao.patchStock(productId, requestQuantity);
    }
}
