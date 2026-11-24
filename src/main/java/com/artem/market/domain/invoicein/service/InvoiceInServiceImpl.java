package com.artem.market.domain.invoicein.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.invoicein.dto.InvoiceInItemRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInRequest;
import com.artem.market.domain.invoicein.entity.InvoiceIn;
import com.artem.market.domain.invoicein.entity.InvoiceInItem;
import com.artem.market.domain.invoicein.mapper.InvoiceInMapper;
import com.artem.market.domain.invoicein.repository.InvoiceInRepository;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceInServiceImpl implements InvoiceInService {

    private final InvoiceInRepository repository;
    private final InvoiceInMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<InvoiceIn> getAll() { return repository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public InvoiceIn getById(Long id) {
        return findInvoiceIn(id);
    }

    @Override
    @Transactional
    public InvoiceIn create(InvoiceInRequest request,
                            Company company,
                            Partner partner,
                            Contract contract,
                            Warehouse warehouse,
                            Map<Long, Product> productsMap) {

        InvoiceIn invoiceIn = mapper.toEntity(request);
        invoiceIn.setCompany(company);
        invoiceIn.setWarehouse(warehouse);
        invoiceIn.setPartner(partner);
        invoiceIn.setContract(contract);

        if (request.getItems() != null) {
            populateItems(invoiceIn, request.getItems(), productsMap);
        }


        return repository.save(invoiceIn);
    }

    @Override
    @Transactional
    public InvoiceIn update(Long id,
                            InvoiceInRequest request,
                            Company company,
                            Partner partner,
                            Contract contract,
                            Warehouse warehouse
            ,
                            Map<Long, Product> productsMap) {
        InvoiceIn existing = findInvoiceIn(id);

        boolean targetIsDeleted = request.getDeleted() != null
                ? request.getDeleted()
                : existing.isDeleted();

        mapper.updateEntityFromRequest(request, existing);
        existing.setCompany(company);
        existing.setWarehouse(warehouse);
        existing.setPartner(partner);
        existing.setContract(contract);
        existing.setDeleted(targetIsDeleted);

        if (request.getItems() != null) {
            existing.clearItems();
            populateItems(existing, request.getItems(), productsMap);
        }

        return repository.save(existing);
    }

    @Override
    @Transactional
    public InvoiceIn toggleDeleteStatus(Long id) {
        InvoiceIn invoiceIn = findInvoiceIn(id);
        invoiceIn.setDeleted(!invoiceIn.isDeleted());

        return repository.save(invoiceIn);
    }

    @Override
    @Transactional
    public InvoiceIn togglePostStatus(Long id) {
        InvoiceIn invoiceIn = findInvoiceIn(id);
        invoiceIn.setPosted(!invoiceIn.isPosted());
        return repository.save(invoiceIn);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InvoiceIn invoiceIn = findInvoiceIn(id);
        repository.delete(invoiceIn);
    }

    private InvoiceIn findInvoiceIn(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Инвойс не найден"));
    }

    private void populateItems(InvoiceIn invoiceIn,
                               List<InvoiceInItemRequest> itemRequests,
                               Map<Long, Product> productsMap) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            invoiceIn.recalculateTotalAmount();
            return;
        }

        int lineNum = 1;
        for (InvoiceInItemRequest itemReq : itemRequests) {
            Product product = productsMap.get(itemReq.getProductId());
            if (product == null) {
                throw new BusinessRuleException("Товар с ID " + itemReq.getProductId() + " не найден");
            }

            InvoiceInItem item = mapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setLineNumber(lineNum++);
            item.calculateAmount();

            invoiceIn.addItem(item);
        }

        invoiceIn.recalculateTotalAmount();
    }
}


