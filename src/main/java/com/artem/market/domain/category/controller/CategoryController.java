package com.artem.market.domain.category.controller;

import com.artem.market.domain.category.dto.CategoryRequest;
import com.artem.market.domain.category.dto.CategoryResponse;
import com.artem.market.domain.category.entity.Category;
import com.artem.market.domain.category.mapper.CategoryMapper;
import com.artem.market.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "2. Catalog category")
public class CategoryController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    @GetMapping
    @Operation(summary = "Get list of all categories")
    public List<CategoryResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public CategoryResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return mapper.toResponse(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category updated = service.update(id, request);
        return mapper.toResponse(updated);
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle category deletion status (Delete/Restore)")
    public CategoryResponse toggleDelete(@PathVariable Long id) {
        Category updatedEntity = service.toggleDeleteStatus(id);
        return mapper.toResponse(updatedEntity);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}