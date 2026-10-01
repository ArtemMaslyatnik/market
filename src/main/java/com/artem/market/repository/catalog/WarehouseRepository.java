package com.artem.market.repository.catalog;

import com.artem.market.model.catalog.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByParentId(Long parentId);
}