package com.artem.market.repository.catalog;

import com.artem.market.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Автоматически сгенерирует эффективный SQL: SELECT EXISTS(SELECT 1 FROM products WHERE parent_id = ?)
    boolean existsByParentId(Long parentId);
}