package com.marketplace.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request to add an item to cart.
 * Only productId and quantity are required - product details are fetched from
 * product service.
 */
@Data
public class AddToCartRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
