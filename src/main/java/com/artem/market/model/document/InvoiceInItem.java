package com.artem.market.model.document;

import com.artem.market.model.base.AbstractDocumentItem;
import com.artem.market.model.catalog.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "doc_invoice_in_items")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceInItem extends AbstractDocumentItem {

    // 1. Связь с шапкой прихода (document_invoice_in из Django)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceIn invoice;

    // 2. Закупаемый товар / Номенклатура (goods из Django)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 3. Закупочная цена товара (price из Django)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    // 4. Количество (quantity из Django с точностью до 3 знаков)
    @Column(nullable = false, precision = 20, scale = 3)
    private BigDecimal quantity;

    // 5. Сумма по строке (amount из Django)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Автоматический расчет суммы строки (Цена * Количество)
     * с каноническим банковским округлением половины к ближайшему чётному числу.
     */
    public void calculateAmount() {
        if (this.price != null && this.quantity != null) {
            this.amount = this.price.multiply(this.quantity)
                    .setScale(2, java.math.RoundingMode.HALF_EVEN);
        }
    }
}