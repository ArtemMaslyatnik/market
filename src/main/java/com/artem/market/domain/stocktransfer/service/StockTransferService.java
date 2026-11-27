package com.artem.market.domain.stocktransfer.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.stocktransfer.dto.StockTransferRequest;
import com.artem.market.domain.stocktransfer.entity.StockTransfer;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.warehouse.entity.Warehouse;

import java.util.List;
import java.util.Map;

public interface StockTransferService {
    List<StockTransfer> getAll();
    StockTransfer getById(Long id);
    StockTransfer create(StockTransferRequest request, Company company, Warehouse sourceWarehouse, Warehouse destinationWarehouse, Map<Long, Product> productsMap);
    StockTransfer update(Long id, StockTransferRequest request, Company company,  Warehouse sourceWarehouse, Warehouse destinationWarehouse, Map<Long, Product> productsMap);
    void delete(Long id);
    StockTransfer toggleDeleteStatus(Long id);
    StockTransfer togglePostStatus(Long id);
}