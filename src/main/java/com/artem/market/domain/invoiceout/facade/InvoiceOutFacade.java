package com.artem.market.domain.invoiceout.facade;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.service.CompanyService;
import com.artem.market.domain.invoiceout.dto.InvoiceOutItemRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutResponse;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.contract.service.ContractService;
import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import com.artem.market.domain.invoiceout.mapper.InvoiceOutMapper;
import com.artem.market.domain.invoiceout.service.InvoiceOutService;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.service.PartnerService;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.product.service.ProductService;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.domain.warehouse.service.WarehouseService;
import com.artem.market.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceOutFacade {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final PartnerService partnerService;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final InvoiceOutService invoiceOutService;
    private final InvoiceOutMapper invoiceOutMapper;

    @Transactional(readOnly = true)
    public List<InvoiceOutResponse> getAllInvoicesOut() {
        return invoiceOutService.getAll().stream()
                .map(invoiceOutMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceOutResponse getInvoiceOutById(Long id) {
        return invoiceOutMapper.toResponse(invoiceOutService.getById(id));
    }

    @Transactional
    public InvoiceOutResponse createInvoiceOut(InvoiceOutRequest request) {
        Company company = request.getCompanyId() != null
                ? companyService.getById(request.getCompanyId())
                : null;
        Warehouse warehouse = request.getWarehouseId() != null
                ? warehouseService.getById(request.getWarehouseId())
                : null;
        Partner partner = request.getPartnerId() != null
                ? findValidPartner(request.getPartnerId())
                : null;
        Contract contract = request.getContractId() != null
                ? contractService.getById(request.getContractId())
                : null;

        Map<Long, Product> productsMap = loadProductsMap(request.getItems());

        InvoiceOut invoiceIn = invoiceOutService.create(request, company, partner, contract, warehouse, productsMap);
        return invoiceOutMapper.toResponse(invoiceIn);
    }

    @Transactional
    public InvoiceOutResponse updateInvoiceOut(Long id, InvoiceOutRequest request) {
        InvoiceOut existing = invoiceOutService.getById(id);

        Company company = request.getCompanyId() != null
                ? companyService.getById(request.getCompanyId())
                : existing.getCompany();
        Warehouse warehouse = request.getWarehouseId() != null
                ? warehouseService.getById(request.getWarehouseId())
                : existing.getWarehouse();
        Partner partner = request.getPartnerId() != null
                ? findValidPartner(request.getPartnerId())
                : existing.getPartner();
        Contract contract = request.getContractId() != null
                ? contractService.getById(request.getContractId())
                : existing.getContract();
        Map<Long, Product> productsMap = loadProductsMap(request.getItems());

        InvoiceOut updated = invoiceOutService.update(id, request, company, partner, contract, warehouse, productsMap);
        return invoiceOutMapper.toResponse(updated);
    }

    @Transactional
    public InvoiceOutResponse toggleInvoiceOutStatusDelete(Long id) {
        InvoiceOut toggledInvoiceIn = invoiceOutService.toggleDeleteStatus(id);
        return invoiceOutMapper.toResponse(toggledInvoiceIn);
    }

    @Transactional
    public InvoiceOutResponse toggleInvoiceOutStatusPost(Long id) {
        InvoiceOut toggledInvoiceOut = invoiceOutService.togglePostStatus(id);
        return invoiceOutMapper.toResponse(toggledInvoiceOut);
    }

    @Transactional
    public void deleteInvoiceOut(Long id) {
        invoiceOutService.delete(id);
    }

    private Partner findValidPartner(Long partnerId) {
        Partner partner = partnerService.getById(partnerId);

        if (partner.isGroup()) {
            throw new BusinessRuleException("Инвойс не может быть оформлен на группу! Выберите конкретного контрагента.");
        }
        return partner;
    }

    private Map<Long, Product> loadProductsMap(List<InvoiceOutItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        return items.stream()
                .map(InvoiceOutItemRequest::getProductId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), productService::getById));
    }
}