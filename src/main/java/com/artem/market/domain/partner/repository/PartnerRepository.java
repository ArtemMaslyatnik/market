package com.artem.market.domain.partner.repository;

import com.artem.market.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    // Автоматически сгенерирует эффективный SQL: SELECT EXISTS(SELECT 1 FROM products WHERE parent_id = ?)
    boolean existsByParentId(Long parentId);
}