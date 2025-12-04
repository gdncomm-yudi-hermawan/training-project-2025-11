package com.marketplace.product.command;

import com.marketplace.common.command.Command;
import com.marketplace.product.document.Product;
import com.marketplace.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProductByIdCommand implements Command<Product> {

    private final ProductService productService;
    private final String productId;

    @Override
    public Product execute() {
        return productService.getProductById(productId);
    }
}
