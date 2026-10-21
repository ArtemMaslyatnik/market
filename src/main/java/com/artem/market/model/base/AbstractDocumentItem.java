package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass // Базовый класс для всех табличных частей документов
@Getter @Setter
public abstract class AbstractDocumentItem extends AbstractBaseEntity {

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
}