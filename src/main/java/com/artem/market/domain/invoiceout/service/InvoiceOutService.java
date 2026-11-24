package com.artem.market.domain.invoiceout.service;

import com.artem.market.domain.invoiceout.dto.InvoiceOutRequest;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.warehouse.entity.Warehouse;

import java.util.List;
import java.util.Map;

public interface InvoiceOutService {
    List<InvoiceOut> getAll();
    InvoiceOut getById(Long id);
    InvoiceOut create(InvoiceOutRequest request, Company company, Partner partner, Contract contract, Warehouse warehouse, Map<Long, Product> productsMap);
    InvoiceOut update(Long id, InvoiceOutRequest request, Company company, Partner partner, Contract contract, Warehouse warehouse, Map<Long, Product> productsMap);
    void delete(Long id);
    InvoiceOut toggleDeleteStatus(Long id);
    InvoiceOut togglePostStatus(Long id);
}