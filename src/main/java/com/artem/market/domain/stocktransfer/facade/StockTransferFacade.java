package com.artem.market.domain.stocktransfer.facade;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.service.CompanyService;
import com.artem.market.domain.stocktransfer.dto.StockTransferItemRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferResponse;
import com.artem.market.domain.stocktransfer.entity.StockTransfer;
import com.artem.market.domain.stocktransfer.mapper.StockTransferMapper;
import com.artem.market.domain.stocktransfer.service.StockTransferService;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.product.service.ProductService;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.domain.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockTransferFacade {

    private final CompanyService companyService;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final StockTransferService stockTransferService;
    private final StockTransferMapper stockTransferMapper;

    @Transactional(readOnly = true)
    public List<StockTransferResponse> getAllStockTransfer() {
        return stockTransferService.getAll().stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockTransferResponse getStockTransferById(Long id) {
        return stockTransferMapper.toResponse(stockTransferService.getById(id));
    }

    @Transactional
    public StockTransferResponse createStockTransfer(StockTransferRequest request) {
        Company company = request.getCompanyId() != null
                ? companyService.getById(request.getCompanyId())
                : null;
        Warehouse sourceWarehouse = request.getSourceWarehouseId() != null
                ? warehouseService.getById(request.getSourceWarehouseId())
                : null;
        Warehouse destinationWarehouse = request.getDestinationWarehouseId() != null
                ? warehouseService.getById(request.getDestinationWarehouseId())
                : null;

        Map<Long, Product> productsMap = loadProductsMap(request.getItems());

        StockTransfer stockTransfer = stockTransferService.create(request, company, sourceWarehouse, destinationWarehouse, productsMap);
        return stockTransferMapper.toResponse(stockTransfer);
    }

    @Transactional
    public StockTransferResponse updateStockTransfer(Long id, StockTransferRequest request) {
        StockTransfer existing = stockTransferService.getById(id);

        Company company = request.getCompanyId() != null
                ? companyService.getById(request.getCompanyId())
                : existing.getCompany();
        Warehouse sourceWarehouse = request.getSourceWarehouseId() != null
                ? warehouseService.getById(request.getSourceWarehouseId())
                : existing.getSourceWarehouse();
        Warehouse destinationWarehouse = request.getDestinationWarehouseId() != null
                ? warehouseService.getById(request.getDestinationWarehouseId())
                : existing.getDestinationWarehouse();
        Map<Long, Product> productsMap = loadProductsMap(request.getItems());

        StockTransfer updated = stockTransferService.update(id, request, company, sourceWarehouse, destinationWarehouse, productsMap);
        return stockTransferMapper.toResponse(updated);
    }

    @Transactional
    public StockTransferResponse toggleStockTransferStatusDelete(Long id) {
        StockTransfer toggledStockTransfer = stockTransferService.toggleDeleteStatus(id);
        return stockTransferMapper.toResponse(toggledStockTransfer);
    }

    @Transactional
    public StockTransferResponse toggleStockTransferStatusPost(Long id) {
        StockTransfer toggledStockTransfer = stockTransferService.togglePostStatus(id);
        return stockTransferMapper.toResponse(toggledStockTransfer);
    }

    @Transactional
    public void deleteStockTransfer(Long id) {
        stockTransferService.delete(id);
    }


    private Map<Long, Product> loadProductsMap(List<StockTransferItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        return items.stream()
                .map(StockTransferItemRequest::getProductId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), productService::getById));
    }
}