package com.artem.market.domain.contract.service;

import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.entity.Contract;

import java.util.List;

public interface ContractService {
    List<Contract> getAll();
    Contract getById(Long id);
    Contract create(ContractRequest request);
    Contract update(Long id, ContractRequest request);
    Contract delete(Long id);
}
