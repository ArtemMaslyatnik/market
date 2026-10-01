package com.artem.market.model.catalog;

import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.model.base.AbstractCatalog;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class Contract extends AbstractCatalog {

    @Column(name = "contract_date", nullable = false)
    private LocalDateTime contractDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}