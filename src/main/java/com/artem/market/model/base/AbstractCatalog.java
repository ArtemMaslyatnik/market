// Файл: AbstractCatalog.java
package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class AbstractCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // Уникальный код/артикул

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup = false; // Это папка/группа?

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // Мягкое удаление

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}