package com.artem.market.model.catalog;

import com.artem.market.model.base.AbstractCatalog;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company extends AbstractCatalog {

    // Канонично: сущность сама управляет своими внутренними инвариантами
    public void initAsCompany() {
        this.setIsGroup(false);
        this.setParent(null);
    }
}