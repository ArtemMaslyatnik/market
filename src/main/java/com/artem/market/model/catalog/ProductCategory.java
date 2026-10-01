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
@Table(name = "product_categories")
@Getter
@Setter
@NoArgsConstructor
public class ProductCategory extends AbstractCatalog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Защита от бесконечной циклической вложенности в JSON
    private List<ProductCategory> children = new ArrayList<>();

    // Зеркальное поле для двусторонней связи Многие-ко-многим
    @ManyToMany(mappedBy = "categories") // Указывает на имя поля в классе Product
    private List<Product> products = new ArrayList<>();
}