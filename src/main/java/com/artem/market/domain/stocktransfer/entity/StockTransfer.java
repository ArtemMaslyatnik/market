package com.artem.market.domain.stocktransfer.entity;

import com.artem.market.model.base.AbstractDocument;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doc_stock_transfers")
@Getter
@Setter
@NoArgsConstructor
public class StockTransfer extends AbstractDocument {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id")
    @JsonIgnore
    private Warehouse sourceWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id")
    @JsonIgnore
    private Warehouse destinationWarehouse;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransferItem> items = new ArrayList<>();

    public void addItem(StockTransferItem item) {
        this.items.add(item);
        item.setTransfer(this);
    }

    public void clearItems() {
        this.items.forEach(item -> item.setTransfer(null));
        this.items.clear();
    }
}