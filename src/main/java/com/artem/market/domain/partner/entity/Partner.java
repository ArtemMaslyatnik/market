package com.artem.market.domain.partner.entity;

import com.artem.market.model.base.AbstractCatalog;
import com.artem.market.model.catalog.Contract;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
public class Partner extends AbstractCatalog {

    // 1. Ссылка на родительскую группу (папку контрагентов)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Partner parent;

    // 2. Список дочерних элементов/папок внутри этой группы
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Защита от бесконечной циклической вложенности в JSON
    private List<Partner> children = new ArrayList<>();

    // 3. Связь с договорами
    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Игнорируем при базовом запросе контрагента, чтобы не грузить лишние данные
    private List<Contract> contracts = new ArrayList<>();
}