package com.artem.market.domain.stocktransfer.repository;

import com.artem.market.domain.stocktransfer.entity.StockTransfer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    @Override
    @EntityGraph(attributePaths = {"company", "sourceWarehouse", "destinationWarehouse"})
    List<StockTransfer> findAll();

    @Override
    @EntityGraph(attributePaths = {"company", "sourceWarehouse", "destinationWarehouse", "items", "items.product"})
    Optional<StockTransfer> findById(Long id);
}