package com.artem.market.domain.invoicein.controller;

import com.artem.market.domain.invoicein.dto.InvoiceInRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInResponse;
import com.artem.market.domain.invoicein.facade.InvoiceInFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice-in")
@RequiredArgsConstructor
@Tag(name = "1. Document incoming invoice")
public class InvoiceInController {

    private final InvoiceInFacade invoiceInFacade;

    @GetMapping
    @Operation(summary = "Get list of all incoming invoice")
    public ResponseEntity <List<InvoiceInResponse>> getAll() {
        return ResponseEntity.ok(invoiceInFacade.getAllInvoicesIn());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get incoming invoice by ID")
    public ResponseEntity<InvoiceInResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceInFacade.getInvoiceInById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new incoming invoice")
    public ResponseEntity<InvoiceInResponse> create(@Valid @RequestBody InvoiceInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceInFacade.createInvoiceIn(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing incoming invoice")
    public ResponseEntity<InvoiceInResponse> update(@PathVariable Long id, @Valid @RequestBody InvoiceInRequest request) {
        return ResponseEntity.ok(invoiceInFacade.updateInvoiceIn(id, request));
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle incoming invoice deletion status (Delete/Restore)")
    public ResponseEntity<InvoiceInResponse> toggleDelete(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceInFacade.toggleInvoiceInStatusDelete(id));
    }

    @PatchMapping("/{id}/toggle-post")
    @Operation(summary = "Toggle incoming invoice post status (Post/Unpost)")
    public ResponseEntity<InvoiceInResponse> togglePost(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceInFacade.toggleInvoiceInStatusPost(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a incoming invoice")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceInFacade.deleteInvoiceIn(id);
        return ResponseEntity.noContent().build();
    }
}