package com.flab.just_10_minutes.product.service;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.dto.ProductDto;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
