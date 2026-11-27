package com.artem.market.domain.stocktransfer.entity;

import com.artem.market.model.base.AbstractDocumentItem;
import com.artem.market.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "doc_stock_transfer_items")
@Getter
@Setter
@NoArgsConstructor
public class StockTransferItem extends AbstractDocumentItem {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private StockTransfer transfer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(precision = 20, scale = 3)
    private BigDecimal quantity;
}