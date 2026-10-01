package com.artem.market.controller.catalog;

import com.artem.market.model.catalog.Company;
import com.artem.market.repository.catalog.CompanyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "1. Справочник Организаций")
public class CompanyController {

    private final CompanyRepository repository;

    public CompanyController(CompanyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Company> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Company getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Организация не найдена"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company entity) {
        // Гарантируем, что организация никогда не станет группой/папкой
        entity.setIsGroup(false);
        entity.setParent(null);

        return repository.save(entity);
    }

    @PutMapping("/{id}")
    public Company update(@PathVariable Long id, @RequestBody Company entity) {
        Company existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Организация не найдена"));
        existing.setName(entity.getName());
        existing.setCode(entity.getCode());

        // На всякий случай жестко фиксируем, что это элемент, а не папка
        existing.setIsGroup(false);
        existing.setIsParent(null);
        existing.setIsDeleted(entity.getIsDeleted() != null ? entity.getIsDeleted() : existing.getIsDeleted());
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Мягкое логическое удаление
    @Operation(summary = "Пометить организацию на удаление")
    public Company delete(@PathVariable Long id) {
        Company existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Организация не найдена"));

        // Просто помечаем флагом удаление (без проверок на дочерние элементы)
        existing.setIsDeleted(true);

        return repository.save(existing);
    }
}