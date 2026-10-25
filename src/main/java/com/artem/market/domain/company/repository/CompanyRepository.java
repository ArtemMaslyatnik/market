package com.artem.market.domain.company.repository;

import com.artem.market.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanyRepository extends JpaRepository<Company, Long> {
}