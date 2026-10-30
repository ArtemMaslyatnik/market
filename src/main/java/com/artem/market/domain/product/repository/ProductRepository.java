package com.artem.market.domain.product.repository;

import com.artem.market.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByParentId(Long parentId);
}