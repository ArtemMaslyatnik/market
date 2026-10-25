package com.artem.market.domain.warehouse.service;

import com.artem.market.domain.warehouse.dto.WarehouseRequest;
import com.artem.market.domain.warehouse.entity.Warehouse;
import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAll();
    Warehouse getById(Long id);
    Warehouse create(WarehouseRequest request);
    Warehouse update(Long id, WarehouseRequest request);
    Warehouse delete(Long id);
}