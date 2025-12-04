package com.marketplace.product.command;

import com.marketplace.common.command.Command;
import com.marketplace.product.document.Product;
import com.marketplace.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class SearchProductsCommand implements Command<Page<Product>> {

    private final ProductService productService;
    private final String name;
    private final Pageable pageable;

    @Override
    public Page<Product> execute() {
        return productService.searchProducts(name, pageable);
    }
}
