package com.flab.just_10_minutes.Product.controller;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.dto.ProductDto;
import com.flab.just_10_minutes.Product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/enroll")
    public ResponseEntity<Product> enroll(@RequestBody @Valid ProductDto productDto) {
        Product product = productService.save(productDto);

        return ResponseEntity.ok(product);
    }
}
