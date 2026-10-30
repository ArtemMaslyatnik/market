package com.artem.market.domain.contract.controller;

import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.dto.ContractResponse;
import com.artem.market.domain.contract.facade.ContractFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Tag(name = "1. Catalog contracts")
public class ContractController {

    private final ContractFacade contractFacade;

    @GetMapping
    @Operation(summary = "Get list of all contracts")
    public ResponseEntity <List<ContractResponse>> getAll() {
        return ResponseEntity.ok(contractFacade.getAllContracts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contract by ID")
    public ResponseEntity<ContractResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contractFacade.getContractById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new contract")
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody ContractRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contractFacade.createContract(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing contract")
    public ResponseEntity<ContractResponse> update(@PathVariable Long id, @Valid @RequestBody ContractRequest request) {
        return ResponseEntity.ok(contractFacade.updateContract(id, request));
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle contract deletion status (Delete/Restore)")
    public ResponseEntity<ContractResponse> toggleDelete(@PathVariable Long id) {
        return ResponseEntity.ok(contractFacade.toggleContractStatus(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contract")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contractFacade.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}