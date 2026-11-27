package com.artem.market.domain.invoicein.facade;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.company.service.CompanyService;
import com.artem.market.domain.invoicein.dto.InvoiceInItemRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInResponse;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.contract.service.ContractService;
import com.artem.market.domain.invoicein.entity.InvoiceIn;
import com.artem.market.domain.invoicein.mapper.InvoiceInMapper;
import com.artem.market.domain.invoicein.service.InvoiceInService;
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
public class InvoiceInFacade {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final PartnerService partnerService;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final InvoiceInService invoiceInService;
    private final InvoiceInMapper invoiceInMapper;

    @Transactional(readOnly = true)
    public List<InvoiceInResponse> getAllInvoicesIn() {
        return invoiceInService.getAll().stream()
                .map(invoiceInMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceInResponse getInvoiceInById(Long id) {
        return invoiceInMapper.toResponse(invoiceInService.getById(id));
    }

    @Transactional
    public InvoiceInResponse createInvoiceIn(InvoiceInRequest request) {
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

        InvoiceIn invoiceIn = invoiceInService.create(request, company, partner, contract, warehouse, productsMap);
        return invoiceInMapper.toResponse(invoiceIn);
    }

    @Transactional
    public InvoiceInResponse updateInvoiceIn(Long id, InvoiceInRequest request) {
        InvoiceIn existing = invoiceInService.getById(id);

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

        InvoiceIn updated = invoiceInService.update(id, request, company, partner, contract, warehouse, productsMap);
        return invoiceInMapper.toResponse(updated);
    }

    @Transactional
    public InvoiceInResponse toggleInvoiceInStatusDelete(Long id) {
        InvoiceIn toggledInvoiceIn = invoiceInService.toggleDeleteStatus(id);
        return invoiceInMapper.toResponse(toggledInvoiceIn);
    }

    @Transactional
    public InvoiceInResponse toggleInvoiceInStatusPost(Long id) {
        InvoiceIn toggledInvoiceIn = invoiceInService.togglePostStatus(id);
        return invoiceInMapper.toResponse(toggledInvoiceIn);
    }

    @Transactional
    public void deleteInvoiceIn(Long id) {
        invoiceInService.delete(id);
    }

    private Partner findValidPartner(Long partnerId) {
        Partner partner = partnerService.getById(partnerId);

        if (partner.isGroup()) {
            throw new BusinessRuleException("Инвойс не может быть оформлен на группу! Выберите конкретного контрагента.");
        }
        return partner;
    }

    private Map<Long, Product> loadProductsMap(List<InvoiceInItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        return items.stream()
                .map(InvoiceInItemRequest::getProductId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), productService::getById));
    }
}