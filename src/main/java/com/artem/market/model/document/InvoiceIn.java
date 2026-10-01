package com.artem.market.model.document;

import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.model.base.AbstractDocument;
import com.artem.market.model.catalog.*;
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

    // 1. Организация (Наша фирма, привязана как related_name='companies')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // 2. Контрагент (Поставщик)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    // 3. Договор с поставщиком
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    // 4. Склад поступления (warehouse из Django)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // 5. Общая сумма прихода (total из Django)
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // 6. Связь с табличной частью закупаемых товаров
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceInItem> items = new ArrayList<>();

    // Удобный метод для добавления строк в документ
    public void addItem(InvoiceInItem item) {
        this.items.add(item);
        item.setInvoice(this);
    }
}