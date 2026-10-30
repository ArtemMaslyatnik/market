package com.artem.market.domain.product.controller;

import com.artem.market.domain.product.dto.ProductRequest;
import com.artem.market.domain.product.dto.ProductResponse;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.product.mapper.ProductMapper;
import com.artem.market.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "1. Catalog products")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;


    @GetMapping
    @Operation(summary = "Get list of all product")
    public List<ProductResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ProductResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return mapper.toResponse(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        Product updated = service.update(id, request);
        return mapper.toResponse(updated);
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle product deletion status (Delete/Restore)")
    public ProductResponse toggleDelete(@PathVariable Long id) {
        Product updatedEntity = service.toggleDeleteStatus(id);
        return mapper.toResponse(updatedEntity);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}