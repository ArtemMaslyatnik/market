package com.artem.market.domain.invoiceout.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.invoiceout.dto.InvoiceOutItemRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutRequest;
import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import com.artem.market.domain.invoiceout.entity.InvoiceOutItem;
import com.artem.market.domain.invoiceout.mapper.InvoiceOutMapper;
import com.artem.market.domain.invoiceout.repository.InvoiceOutRepository;
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
public class InvoiceOutServiceImpl implements InvoiceOutService {

    private final InvoiceOutRepository repository;
    private final InvoiceOutMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<InvoiceOut> getAll() { return repository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public InvoiceOut getById(Long id) {
        return findInvoiceOut(id);
    }

    @Override
    @Transactional
    public InvoiceOut create(InvoiceOutRequest request,
                            Company company,
                            Partner partner,
                            Contract contract,
                            Warehouse warehouse,
                            Map<Long, Product> productsMap) {

        InvoiceOut invoiceOut = mapper.toEntity(request);
        invoiceOut.setCompany(company);
        invoiceOut.setWarehouse(warehouse);
        invoiceOut.setPartner(partner);
        invoiceOut.setContract(contract);

        if (request.getItems() != null) {
            populateItems(invoiceOut, request.getItems(), productsMap);
        }


        return repository.save(invoiceOut);
    }

    @Override
    @Transactional
    public InvoiceOut update(Long id,
                            InvoiceOutRequest request,
                            Company company,
                            Partner partner,
                            Contract contract,
                            Warehouse warehouse
            ,
                            Map<Long, Product> productsMap) {
        InvoiceOut existing = findInvoiceOut(id);

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
    public InvoiceOut toggleDeleteStatus(Long id) {
        InvoiceOut invoiceOut = findInvoiceOut(id);
        invoiceOut.setDeleted(!invoiceOut.isDeleted());

        return repository.save(invoiceOut);
    }

    @Override
    @Transactional
    public InvoiceOut togglePostStatus(Long id) {
        InvoiceOut invoiceOut = findInvoiceOut(id);
        invoiceOut.setPosted(!invoiceOut.isPosted());
        return repository.save(invoiceOut);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InvoiceOut invoiceOut = findInvoiceOut(id);
        repository.delete(invoiceOut);
    }

    private InvoiceOut findInvoiceOut(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Инвойс не найден"));
    }

    private void populateItems(InvoiceOut invoiceOut,
                               List<InvoiceOutItemRequest> itemRequests,
                               Map<Long, Product> productsMap) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            invoiceOut.recalculateTotalAmount();
            return;
        }

        int lineNum = 1;
        for (InvoiceOutItemRequest itemReq : itemRequests) {
            Product product = productsMap.get(itemReq.getProductId());
            if (product == null) {
                throw new BusinessRuleException("Товар с ID " + itemReq.getProductId() + " не найден");
            }

            InvoiceOutItem item = mapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setLineNumber(lineNum++);
            item.calculateAmount();

            invoiceOut.addItem(item);
        }

        invoiceOut.recalculateTotalAmount();
    }

}


