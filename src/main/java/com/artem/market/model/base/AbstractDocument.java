package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "doc_base")
@Inheritance(strategy = InheritanceType.JOINED) // Стратегия связи таблиц-наследников
@Getter @Setter
public abstract class AbstractDocument extends AbstractBaseEntity{


    @Column(name = "doc_number", nullable = false, unique = true, length = 10)
    private String number;

    @Column(name = "is_posted", nullable = false)
    private boolean posted = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

}