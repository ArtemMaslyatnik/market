package com.artem.market.domain.warehouse.repository;

import com.artem.market.domain.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;



public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
     boolean existsByParentId(Long parentId);
}