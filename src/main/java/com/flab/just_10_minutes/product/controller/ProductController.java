package com.flab.just_10_minutes.product.controller;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.dto.ProductDto;
import com.flab.just_10_minutes.product.service.ProductService;
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
