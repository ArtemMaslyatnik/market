package com.artem.market.domain.contract.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.partner.entity.Partner;

import java.util.List;

public interface ContractService {
    List<Contract> getAll();
    Contract getById(Long id);
    Contract create(ContractRequest request, Company company, Partner partner);
    Contract update(Long id, ContractRequest request, Company company, Partner partner);
    Contract delete(Long id);
    Contract toggleDeleteStatus(Long id);
}
