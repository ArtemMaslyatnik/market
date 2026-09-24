package com.artem.market.model.document;

import com.artem.market.model.base.AbstractDocumentItem;
import com.artem.market.model.catalog.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "doc_invoice_out_items")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceOutItem extends AbstractDocumentItem {

    // 1. Связь с шапкой документа (Расходной накладной)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceOut invoice;

    // 2. Связь с товаром
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 3. Количество товара (с точностью до 5 знаков для весового товара)
    @Column(nullable = false, precision = 20, scale = 5)
    private BigDecimal quantity;

    // 4. Сумма по строке
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}