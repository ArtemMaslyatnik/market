package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass // Базовый класс для всех табличных частей документов
@Getter @Setter
public abstract class AbstractDocumentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
}