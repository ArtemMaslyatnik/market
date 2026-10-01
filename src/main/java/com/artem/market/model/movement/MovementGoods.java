package com.artem.market.model.movement;

import com.artem.market.model.base.AbstractMovement;
import com.artem.market.model.catalog.Company; // Импортируем Организацию
import com.artem.market.model.catalog.Product;
import com.artem.market.model.catalog.Warehouse;
import com.artem.market.model.document.InvoiceIn;
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

    // ==========================================
    // АНАЛИТИКА УЧЕТА (Разрезы)
    // ==========================================

    // 1. Организация (Новое каноничное поле)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // 2. Товар / Номенклатура
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 3. Склад
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // 4. Партия (Документ закупки)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private InvoiceIn batch;

    // ==========================================
    // РЕСУРСЫ УЧЕТА (Показатели)
    // ==========================================

    // 5. Количество
    @Column(nullable = false, precision = 20, scale = 3)
    private BigDecimal quantity;

    // 6. Сумма себестоимости
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}