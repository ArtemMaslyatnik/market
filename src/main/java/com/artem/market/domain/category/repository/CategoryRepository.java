package com.artem.market.domain.category.repository;

import com.artem.market.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByParentId(Long parentId);
}