package com.artem.market.model.movement;

import com.artem.market.model.base.AbstractMovement;
import com.artem.market.model.catalog.Product;
import com.artem.market.model.catalog.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "movement_goods")
@Getter
@Setter
@NoArgsConstructor
public class MovementGoods extends AbstractMovement {

    // 1. Аналитика: Какой товар пришел/ушел
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 2. Аналитика: На каком складе произошло движение
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // 3. Ресурс: Количество (precision 20, scale 5 для точного весового учета)
    @Column(nullable = false, precision = 20, scale = 5)
    private BigDecimal quantity;

    // 4. Ресурс: Сумма (финансовая себестоимость или сумма продажи)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}