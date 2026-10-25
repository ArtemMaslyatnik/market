package com.artem.market.model.document;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.warehouse.entity.Warehouse;
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

    // 1. Организация (Наша фирма)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // 2. Контрагент (Клиент / Покупатель)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    // 3. Договор с контрагентом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    // 4. Склад списания
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // 5. Общая сумма документа
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // 6. Связь с табличной частью товаров
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceOutItem> items = new ArrayList<>();

    public void addItem(InvoiceOutItem item) {
        this.items.add(item);
        item.setInvoice(this);
    }
}