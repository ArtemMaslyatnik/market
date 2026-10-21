package com.artem.market.domain.contract.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.repository.CompanyRepository;
import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.contract.mapper.ContractMapper;
import com.artem.market.domain.contract.repository.ContractRepository;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.repository.PartnerRepository;
import com.artem.market.exception.BusinessRuleException;
import com.artem.market.model.base.context.JpaContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;
    private final CompanyRepository companyRepository;
    private final PartnerRepository partnerRepository;
    private final ContractMapper mapper;
    private final JpaContext jpaContext;

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
    public Contract create(ContractRequest request) {
        findCompany(request.getCompanyId());
        findPartner(request.getPartnerId());

        Contract contract = mapper.toEntity(request, jpaContext);

        return repository.save(contract);
    }

    @Override
    @Transactional
    public Contract update(Long id, ContractRequest request) {
        Contract existing = findContract(id);

        if (request.getCompanyId() != null) {
            findCompany(request.getCompanyId());
        }

        if (request.getPartnerId() != null) {
            findPartner(request.getPartnerId());
        }

        mapper.updateEntityFromRequest(request, existing, jpaContext);

        return repository.save(existing);
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

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Компания не найдена"));
    }

    private Partner findPartner(Long id) {
        return partnerRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Контрагент не найден"));
    }
}


