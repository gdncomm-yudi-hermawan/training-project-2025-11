package com.marketplace.product.service;

import com.marketplace.product.document.Product;
import com.marketplace.product.exception.ProductNotFoundException;
import com.marketplace.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void seedProducts() {
        log.info("Seeding products into database");

        if (productRepository.count() > 0) {
            log.info("Products already exist, skipping seed");
            return;
        }

        Product[] products = {
                Product.builder()
                        .name("Laptop")
                        .description("High-performance laptop")
                        .price(BigDecimal.valueOf(999.99))
                        .category("Electronics")
                        .stock(10)
                        .build(),
                Product.builder()
                        .name("Smartphone")
                        .description("Latest smartphone model")
                        .price(BigDecimal.valueOf(699.99))
                        .category("Electronics")
                        .stock(20)
                        .build(),
                Product.builder()
                        .name("Headphones")
                        .description("Noise-cancelling headphones")
                        .price(BigDecimal.valueOf(199.99))
                        .category("Electronics")
                        .stock(15)
                        .build(),
                Product.builder()
                        .name("T-Shirt")
                        .description("Cotton t-shirt")
                        .price(BigDecimal.valueOf(19.99))
                        .category("Clothing")
                        .stock(50)
                        .build(),
                Product.builder()
                        .name("Jeans")
                        .description("Denim jeans")
                        .price(BigDecimal.valueOf(49.99))
                        .category("Clothing")
                        .stock(30)
                        .build()
        };

        productRepository.saveAll(Arrays.asList(products));
        log.info("Seeded {} products successfully", products.length);
    }

    public Page<Product> searchProducts(String name, Pageable pageable) {
        log.info("Searching products with name containing: '{}', page: {}", name, pageable.getPageNumber());

        if (name == null || name.trim().isEmpty()) {
            log.debug("No search term provided, returning all products");
            return productRepository.findAll(pageable);
        }

        Page<Product> results = productRepository.findByNameContainingIgnoreCase(name, pageable);
        log.info("Found {} products matching search term", results.getTotalElements());
        return results;
    }

    public Product getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ProductNotFoundException(id);
                });
    }
}
