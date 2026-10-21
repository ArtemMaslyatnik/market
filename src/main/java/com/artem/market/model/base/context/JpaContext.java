package com.artem.market.model.base.context;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.BeforeMapping;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;
import com.artem.market.model.base.AbstractCatalog;

@Component
public class JpaContext {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Этот метод автоматически вызывается MapStruct ДО начала маппинга.
     * Если id равен null, метод возвращает null, и объект родителя останется null.
     * Если id передан, возвращается легальный Hibernate-прокси.
     */
    @BeforeMapping
    public <T extends AbstractCatalog> T resolveEntity(Long id, @TargetType Class<T> targetType) {
        if (id == null) {
            return null;
        }
        // Возвращает Managed-прокси объект без лишнего SELECT-запроса в БД
        return entityManager.getReference(targetType, id);
    }
}
