package com.artem.market.model.document;

import com.artem.market.model.base.AbstractDocument;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.warehouse.entity.Warehouse;
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

    // 1. Организация (company из Django, related_name='companies_movements')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // 2. Склад-отправитель (warehouse_out из Django, related_name='outgoing_movements')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id", nullable = false)
    private Warehouse sourceWarehouse;

    // 3. Склад-получатель (warehouse_in из Django, related_name='incoming_movements')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id", nullable = false)
    private Warehouse destinationWarehouse;

    // 4. Связь с табличной частью перемещаемых товаров
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransferItem> items = new ArrayList<>();

    // Удобный метод для добавления строк в документ перемещения
    public void addItem(StockTransferItem item) {
        this.items.add(item);
        item.setTransfer(this);
    }
}