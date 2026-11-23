package com.artem.market.domain.invoicein.entity;

import com.artem.market.model.base.AbstractDocumentItem;
import com.artem.market.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "doc_invoice_in_items")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceInItem extends AbstractDocumentItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceIn invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(nullable = false, precision = 20, scale = 3)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;


    @PrePersist
    @PreUpdate
    public void calculateAmount() {
        if (this.price != null && this.quantity != null) {
            this.amount = this.price.multiply(this.quantity)
                    .setScale(2, RoundingMode.HALF_EVEN);
        } else {
            this.amount = BigDecimal.ZERO;
        }
    }

}