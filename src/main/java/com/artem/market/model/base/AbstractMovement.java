package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

enum MovementType {
    INCOMING, // Приход (плюс на склад)
    OUTGOING  // Расход (минус со склада)
}

@MappedSuperclass // Базовый класс для таблиц движений
@Getter @Setter
public abstract class AbstractMovement extends AbstractBaseEntity{

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    // 3. Каноничный "Регистратор" (Вместо GenericForeignKey)
    // Связываем напрямую с базовым абстрактным документом.
    // Каждое движение будет точно знать, какой документ его породил.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private AbstractDocument recorder;

    // 4. Порядковый номер строки в документе (если необходим для аналитики)
    @Column(name = "line_number")
    private Integer lineNumber;

    // 5. Точная дата и время проведения транзакции
    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    @PrePersist
    protected void onMovementCreate() {
        movementDate = LocalDateTime.now();
    }
}