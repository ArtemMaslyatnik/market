package com.artem.market.repository.catalog;

import com.artem.market.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Готово! Методы поиска, сохранения и удаления сгенерированы автоматически
}