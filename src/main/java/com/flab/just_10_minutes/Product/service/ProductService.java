package com.flab.just_10_minutes.Product.service;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.dto.ProductDto;
import com.flab.just_10_minutes.Product.infrastructure.ProductDao;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
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
