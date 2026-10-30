package com.artem.market.domain.product.entity;

import com.artem.market.model.base.AbstractCatalog;
import com.artem.market.domain.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractCatalog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Product parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> children = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_to_category", // Имя промежуточной таблицы в БД
            joinColumns = @JoinColumn(name = "product_id"), // Внешний ключ на товар
            inverseJoinColumns = @JoinColumn(name = "category_id") // Внешний ключ на категорию
    )
    private List<Category> categories = new ArrayList<>();
}