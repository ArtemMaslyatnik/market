package com.artem.market.domain.company.service;

import com.artem.market.domain.company.dto.CompanyRequest;
import com.artem.market.domain.company.entity.Company;


import java.util.List;

public interface CompanyService {
    List<Company> getAll();
    Company getById(Long id);
    Company create(CompanyRequest request);
    Company update(Long id, CompanyRequest request);
    Company toggleDeleteStatus(Long id);
    void delete(Long id);
}