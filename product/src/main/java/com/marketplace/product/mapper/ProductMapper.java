package com.marketplace.product.mapper;

import com.marketplace.product.document.Product;
import com.marketplace.product.dto.response.ProductResponse;

/**
 * Mapper class to convert Product documents to DTOs
 */
public class ProductMapper {

    private ProductMapper() {
        // Prevent instantiation
    }

    /**
     * Convert Product document to ProductResponse DTO
     */
    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stock(product.getStock())
                .build();
    }
}
