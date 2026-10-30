package com.artem.market.domain.partner.controller;

import com.artem.market.domain.partner.dto.PartnerRequest;
import com.artem.market.domain.partner.dto.PartnerResponse;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.mapper.PartnerMapper;
import com.artem.market.domain.partner.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
@Tag(name = "3. Справочник Контрагентов")
public class PartnerController {

    private final PartnerService service;
    private final PartnerMapper mapper; // Внедряем маппер

    @GetMapping
    @Operation(summary = "Получить список всех партнеров и групп")
    public List<PartnerResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить партнера по ID")
    public PartnerResponse getById(@PathVariable Long id) {
        Partner entity = service.getById(id);
        return mapper.toResponse(entity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового партнера или группу")
    public PartnerResponse create(@Valid @RequestBody PartnerRequest request) {
        Partner savedEntity = service.create(request);
        return mapper.toResponse(savedEntity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего партнера или группу")
    public PartnerResponse update(@PathVariable Long id, @Valid @RequestBody PartnerRequest request) {
        Partner updatedEntity = service.update(id, request);
        return mapper.toResponse(updatedEntity);
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle partner deletion status (Delete/Restore)")
    public PartnerResponse toggleDelete(@PathVariable Long id) {
        Partner updatedEntity = service.toggleDeleteStatus(id);
        return mapper.toResponse(updatedEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a partner")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
