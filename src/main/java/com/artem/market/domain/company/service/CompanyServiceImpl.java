package com.artem.market.domain.company.service;

import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.company.dto.CompanyRequest;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.mapper.CompanyMapper;
import com.artem.market.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Company> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Company getById(Long id) {
        return findCompany(id);
    }

    @Override
    @Transactional
    public Company create(CompanyRequest  request) {
        Company company = mapper.toEntity(request);
        return repository.save(company);
    }

    @Override
    @Transactional
    public Company update(Long id, CompanyRequest request) {
        Company existing = findCompany(id);

          mapper.updateEntityFromRequest(request, existing);

        if (request.getDeleted() != null) {
            existing.setDeleted(request.getDeleted());
        }

        return repository.save(existing);
    }

    @Override
    @Transactional
    public Company toggleDeleteStatus(Long id) {
        Company company = findCompany(id);
        company.setDeleted(!company.isDeleted());

        return repository.save(company);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Company existing = findCompany(id);
        repository.delete(existing);
    }

    private Company findCompany(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Компания не найдена"));
    }
}