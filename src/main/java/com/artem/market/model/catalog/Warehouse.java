package com.artem.market.model.catalog;

import com.artem.market.model.base.AbstractCatalog;
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

    // Ссылка на родительскую группу складов (для поддержки иерархии)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Warehouse parent;

    // Список подскладов или элементов внутри группы
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Warehouse> children = new ArrayList<>();
}