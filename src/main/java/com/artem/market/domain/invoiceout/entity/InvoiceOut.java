package com.artem.market.domain.invoiceout.entity;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.model.base.AbstractDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    @JsonIgnore
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonIgnore
    private Warehouse warehouse;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceOutItem> items = new ArrayList<>();

    public void addItem(InvoiceOutItem item) {
        this.items.add(item);
        item.setInvoice(this);
    }

    public void clearItems() {
        this.items.forEach(item -> item.setInvoice(null));
        this.items.clear();
        this.totalAmount = BigDecimal.ZERO;
    }

    public void recalculateTotalAmount() {
        this.totalAmount = this.items.stream()
                .map(InvoiceOutItem::getAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}