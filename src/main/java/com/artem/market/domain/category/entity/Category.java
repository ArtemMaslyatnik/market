package com.artem.market.domain.category.entity;

import com.artem.market.domain.product.entity.Product;
import com.artem.market.model.base.AbstractCatalog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends AbstractCatalog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> children = new ArrayList<>();

    // Зеркальное поле для двусторонней связи Многие-ко-многим
    @ManyToMany(mappedBy = "categories") // Указывает на имя поля в классе Product
    private List<Product> products = new ArrayList<>();
}