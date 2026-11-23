package com.artem.market.model.base;

import com.artem.market.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "doc_base")
@Inheritance(strategy = InheritanceType.JOINED) // Стратегия связи таблиц-наследников
@Getter @Setter
public abstract class AbstractDocument extends AbstractBaseEntity{


    @Column(name = "doc_number", nullable = false, length = 10)
    private String number;

    @Column(name = "doc_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "is_posted", nullable = false)
    private boolean posted = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;


    @PrePersist
    protected void onDocumentCreate() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }

    public void togglePosted() {
        if (!this.posted && this.deleted) {
            throw new BusinessRuleException("Нельзя провести документ, помеченный на удаление.");
        }
        this.posted = !this.posted;
    }

}