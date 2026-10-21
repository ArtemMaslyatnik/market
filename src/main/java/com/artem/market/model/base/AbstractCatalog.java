// Файл: AbstractCatalog.java
package com.artem.market.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class AbstractCatalog extends AbstractBaseEntity{

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "is_group", nullable = false)
    private boolean group = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

}