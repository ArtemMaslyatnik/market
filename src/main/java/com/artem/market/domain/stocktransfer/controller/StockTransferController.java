package com.artem.market.domain.stocktransfer.controller;

import com.artem.market.domain.stocktransfer.dto.StockTransferRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferResponse;
import com.artem.market.domain.stocktransfer.facade.StockTransferFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-transfer")
@RequiredArgsConstructor
@Tag(name = "1.3. Document stock transfer")
public class StockTransferController {

    private final StockTransferFacade stockTransferFacade;

    @GetMapping
    @Operation(summary = "Get list of all stock transfer")
    public ResponseEntity <List<StockTransferResponse>> getAll() {
        return ResponseEntity.ok(stockTransferFacade.getAllStockTransfer());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock transfer by ID")
    public ResponseEntity<StockTransferResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockTransferFacade.getStockTransferById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new stock transfer")
    public ResponseEntity<StockTransferResponse> create(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferFacade.createStockTransfer(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing new stock transfer")
    public ResponseEntity<StockTransferResponse> update(@PathVariable Long id, @Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.ok(stockTransferFacade.updateStockTransfer(id, request));
    }

    @PatchMapping("/{id}/toggle-delete")
    @Operation(summary = "Toggle stock transfer status (Delete/Restore)")
    public ResponseEntity<StockTransferResponse> toggleDelete(@PathVariable Long id) {
        return ResponseEntity.ok(stockTransferFacade.toggleStockTransferStatusDelete(id));
    }

    @PatchMapping("/{id}/toggle-post")
    @Operation(summary = "Toggle stock transfer post status (Post/Unpost)")
    public ResponseEntity<StockTransferResponse> togglePost(@PathVariable Long id) {
        return ResponseEntity.ok(stockTransferFacade.toggleStockTransferStatusPost(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a stock transfer")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockTransferFacade.deleteStockTransfer(id);
        return ResponseEntity.noContent().build();
    }
}