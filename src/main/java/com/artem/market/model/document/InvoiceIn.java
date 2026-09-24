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
@Table(name = "doc_invoices_in")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceIn extends AbstractDocument {

    // 1. Общая сумма прихода
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // 2. Связь с табличной частью (Список закупаемых товаров)
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceInItem> items = new ArrayList<>();

    // Удобный метод для добавления строки в коллекцию
    public void addItem(InvoiceInItem item) {
        this.items.add(item);
        item.setInvoice(this);
    }
}