package com.marketplace.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToCartRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private String productName;
    @NotNull
    private BigDecimal price;
    @Min(1)
    private Integer quantity;
}
