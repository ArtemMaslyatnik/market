package com.artem.market.domain.company.controller;

import com.artem.market.domain.company.dto.CompanyRequest;
import com.artem.market.domain.company.dto.CompanyResponse;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.mapper.CompanyMapper;
import com.artem.market.domain.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "2. Catalog companies")
public class CompanyController {

    private final CompanyService service;
    private final CompanyMapper mapper;

    @GetMapping
    @Operation(summary = "Get list of all companies")
    public List<CompanyResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public CompanyResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new company")
    public CompanyResponse create(@Valid @RequestBody CompanyRequest request) {
        return mapper.toResponse(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing company")
    public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        Company updated = service.update(id, request);
        return mapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a company")
    public CompanyResponse delete(@PathVariable Long id) {

        return mapper.toResponse(service.delete(id));
    }
}