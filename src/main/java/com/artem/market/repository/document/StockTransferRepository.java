package com.artem.market.repository.document;

import com.artem.market.model.document.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
}