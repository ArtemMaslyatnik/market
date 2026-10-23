package com.artem.market.domain.contract.facade;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.service.CompanyService;
import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.dto.ContractResponse;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.contract.mapper.ContractMapper;
import com.artem.market.domain.contract.service.ContractService;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.service.PartnerService;
import com.artem.market.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractFacade {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final PartnerService partnerService;
    private final ContractMapper contractMapper;

    @Transactional(readOnly = true)
    public List<ContractResponse> getAllContracts() {
        return contractService.getAll().stream()
                .map(contractMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContractResponse getContractById(Long id) {
        return contractMapper.toResponse(contractService.getById(id));
    }

    @Transactional
    public ContractResponse createContract(ContractRequest request) {
        Company company = companyService.getById(request.getCompanyId());
        Partner partner = findValidPartner(request.getPartnerId());

        Contract contract = contractService.create(request, company, partner);
        return contractMapper.toResponse(contract);
    }

    @Transactional
    public ContractResponse updateContract(Long id, ContractRequest request) {
        Contract existingContract = contractService.getById(id);

        Company company = existingContract.getCompany();
        Partner partner = existingContract.getPartner();

        Contract updated = contractService.update(id, request, company, partner);
        return contractMapper.toResponse(updated);
    }

    @Transactional
    public ContractResponse toggleContractStatus(Long id) {
        Contract existing = contractService.getById(id);

        if (existing.isDeleted()) {

            if (existing.getCompany() != null && existing.getCompany().isDeleted()) {
                throw new BusinessRuleException(
                        "Нельзя восстановить договор: связанная организация [" + existing.getCompany().getName() + "] удалена!"
                );
            }

            if (existing.getPartner() != null && existing.getPartner().isDeleted()) {
                throw new BusinessRuleException(
                        "Нельзя восстановить договор: связанный контрагент [" + existing.getPartner().getName() + "] удален!"
                );
            }
        }

        Contract toggledContract = contractService.toggleDeleteStatus(id);
        return contractMapper.toResponse(toggledContract);
    }

    @Transactional
    public ContractResponse deleteContract(Long id) {
        Contract deleted = contractService.delete(id);
        return contractMapper.toResponse(deleted);
    }

    private Partner findValidPartner(Long partnerId) {
        Partner partner = partnerService.getById(partnerId);

        if (partner.isGroup()) {
            throw new BusinessRuleException("Договор не может быть оформлен на группу! Выберите конкретного контрагента.");
        }
        return partner;
    }
}