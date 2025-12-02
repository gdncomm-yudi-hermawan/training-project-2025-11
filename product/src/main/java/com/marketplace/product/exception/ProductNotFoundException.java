package com.marketplace.product.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a product is not found
 */
public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException(String productId) {
        super("Product not found with ID: " + productId,
                HttpStatus.NOT_FOUND.value(),
                "PRODUCT_NOT_FOUND");
    }
}
