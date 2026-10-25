package com.artem.market.domain.contract.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.contract.mapper.ContractMapper;
import com.artem.market.domain.contract.repository.ContractRepository;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;
    private final ContractMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<Contract> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contract getById(Long id) {
        return findContract(id);
    }

    @Override
    @Transactional
    public Contract create(ContractRequest request, Company company, Partner partner) {

        Contract contract = mapper.toEntity(request);
        contract.setCompany(company);
        contract.setPartner(partner);
        return repository.save(contract);

    }

    @Override
    @Transactional
    public Contract update(Long id, ContractRequest request, Company company, Partner partner) {
        Contract existing = findContract(id);

        boolean targetIsDeleted = request.getDeleted() != null
                ? request.getDeleted()
                : existing.isDeleted();

        mapper.updateEntityFromRequest(request, existing);
        existing.setDeleted(targetIsDeleted);

        return repository.save(existing);
    }

    @Override
    @Transactional
    public Contract toggleDeleteStatus(Long id) {
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Договор не найден"));

        contract.setDeleted(!contract.isDeleted());

        return repository.save(contract);
    }

    @Override
    @Transactional
    public Contract delete(Long id) {
        Contract contract = findContract(id);
        contract.setDeleted(true);

        return repository.save(contract);
    }

    private Contract findContract(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Договор не найден"));
    }
}


