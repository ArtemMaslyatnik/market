package com.artem.market.model.document;

import com.artem.market.model.base.AbstractDocument;
import com.artem.market.model.catalog.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doc_stock_transfers")
@Getter
@Setter
@NoArgsConstructor
public class StockTransfer extends AbstractDocument {

    // 1. Склад-отправитель
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id", nullable = false)
    private Warehouse sourceWarehouse;

    // 2. Склад-получатель
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id", nullable = false)
    private Warehouse destinationWarehouse;

    // 3. Связь с табличной частью товаров
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransferItem> items = new ArrayList<>();

    // Удобный метод для добавления строки в коллекцию
    public void addItem(StockTransferItem item) {
        this.items.add(item);
        item.setTransfer(this);
    }
}