package com.artem.market.domain.partner.repository;

import com.artem.market.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartnerRepository extends JpaRepository<Partner, Long> {

    boolean existsByParentId(Long parentId);
}