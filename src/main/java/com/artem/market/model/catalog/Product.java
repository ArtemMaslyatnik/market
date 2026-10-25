package com.artem.market.model.catalog;

import com.artem.market.model.base.AbstractCatalog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product extends AbstractCatalog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Product parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Защита от бесконечной циклической вложенности в JSON
    private List<Product> children = new ArrayList<>();

    // НАСТРОЙКА СВЯЗИ МНОГИЕ-КО-МНОГИМ
    @ManyToMany
    @JoinTable(
            name = "product_to_category", // Имя промежуточной таблицы в БД
            joinColumns = @JoinColumn(name = "product_id"), // Внешний ключ на товар
            inverseJoinColumns = @JoinColumn(name = "category_id") // Внешний ключ на категорию
    )
    private List<ProductCategory> categories = new ArrayList<>();
}