package com.artem.market.domain.category.service;

import com.artem.market.domain.category.dto.CategoryRequest;
import com.artem.market.domain.category.entity.Category;


import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category getById(Long id);
    Category create(CategoryRequest request);
    Category update(Long id, CategoryRequest request);
    Category toggleDeleteStatus(Long id);
    void delete(Long id);
}