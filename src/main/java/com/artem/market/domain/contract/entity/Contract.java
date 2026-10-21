package com.artem.market.domain.contract.entity;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.model.base.AbstractCatalog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class Contract extends AbstractCatalog {

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    @JsonIgnore
    private Partner partner;
}