package com.artem.market.domain.warehouse.entity;

import com.artem.market.model.base.AbstractCatalog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
public class Warehouse extends AbstractCatalog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Warehouse parent;

    // Список подскладов или элементов внутри группы
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Warehouse> children = new ArrayList<>();
}