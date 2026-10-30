package com.artem.market.model.document;

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

    // 1. Связь с шапкой документа перемещения
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private StockTransfer transfer;

    // 2. Связь с перемещаемым товаром (Вместо Goods/SubGoods)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 3. Количество перемещаемого товара
    @Column(nullable = false, precision = 20, scale = 5)
    private BigDecimal quantity;
}