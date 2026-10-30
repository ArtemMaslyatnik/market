package com.artem.market.domain.product.service;

import com.artem.market.domain.product.dto.ProductRequest;
import com.artem.market.domain.product.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    Product create(ProductRequest request);
    Product update(Long id, ProductRequest request);
    Product toggleDeleteStatus(Long id);
    void delete(Long id);
}