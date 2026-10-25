package com.artem.market.domain.warehouse.controller;

import com.artem.market.domain.warehouse.dto.WarehouseRequest;
import com.artem.market.domain.warehouse.dto.WarehouseResponse;
import com.artem.market.domain.warehouse.mapper.WarehouseMapper;
import com.artem.market.domain.warehouse.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
@Tag(name = "4. Справочник Складов")
public class WarehouseController {

    private final WarehouseService service;
    private final WarehouseMapper mapper;

    @GetMapping
    @Operation(summary = "Получить список всех складов и групп")
    public List<WarehouseResponse> getAll() {
        return service.getAll().stream()
                .map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить склад по ID")
    public WarehouseResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый склад или группу")
    public WarehouseResponse create(@Valid @RequestBody WarehouseRequest request) {
        return mapper.toResponse(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий склад или группу")
    public WarehouseResponse update(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        return mapper.toResponse(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Пометить склад на мягкое удаление")
    public WarehouseResponse delete(@PathVariable Long id) {
        return mapper.toResponse(service.delete(id));
    }
}
