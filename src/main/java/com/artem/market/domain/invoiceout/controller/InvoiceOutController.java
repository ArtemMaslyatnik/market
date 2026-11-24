package com.artem.market.domain.invoiceout.controller;

import com.artem.market.domain.invoiceout.dto.InvoiceOutRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutResponse;
import com.artem.market.domain.invoiceout.facade.InvoiceOutFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice-out")
@RequiredArgsConstructor
@Tag(name = "1. Document outgoing invoice")
public class InvoiceOutController {

    private final InvoiceOutFacade invoiceOutFacade;

    @GetMapping
    @Operation(summary = "Get list of all outgoing invoice")
    public ResponseEntity <List<InvoiceOutResponse>> getAll() {
        return ResponseEntity.ok(invoiceOutFacade.getAllInvoicesOut());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get outgoing invoice by ID")
    public ResponseEntity<InvoiceOutResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceOutFacade.getInvoiceOutById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new outgoing invoice")
    public ResponseEntity<InvoiceOutResponse> create(@Valid @RequestBody InvoiceOutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceOutFacade.createInvoiceOut(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing outgoing invoice")
    public ResponseEntity<InvoiceOutResponse> update(@PathVariable Long id, @Valid @RequestBody InvoiceOutRequest request) {
        return ResponseEntity.ok(invoiceOutFacade.updateInvoiceOut(id, request));
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle outgoing invoice deletion status (Delete/Restore)")
    public ResponseEntity<InvoiceOutResponse> toggleDelete(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceOutFacade.toggleInvoiceOutStatusDelete(id));
    }

    @PatchMapping("/{id}/toggle-post")
    @Operation(summary = "Toggle outgoing invoice post status (Post/Unpost)")
    public ResponseEntity<InvoiceOutResponse> togglePost(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceOutFacade.toggleInvoiceOutStatusPost(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a outgoing invoice")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceOutFacade.deleteInvoiceOut(id);
        return ResponseEntity.noContent().build();
    }
}