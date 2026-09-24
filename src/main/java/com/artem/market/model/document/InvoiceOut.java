package com.artem.market.model.document;

import com.artem.market.model.base.AbstractDocument;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doc_invoices_out")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceOut extends AbstractDocument {

    // 1. Общая сумма документа
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // 2. Связь с табличной частью (Список строк товаров)
    // CascadeType.ALL означает: если мы удалим документ, его строки удалятся автоматически
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceOutItem> items = new ArrayList<>();

    // Удобный метод для добавления строки в документ
    public void addItem(InvoiceOutItem item) {
        this.items.add(item);
        item.setInvoice(this);
    }
}