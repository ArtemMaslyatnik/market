package com.artem.market.domain.invoiceout.service;

import com.artem.market.domain.invoicein.dto.InvoiceInRequest;
import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.invoicein.entity.InvoiceIn;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.warehouse.entity.Warehouse;

import java.util.List;
import java.util.Map;

public interface InvoiceInService {
    List<InvoiceIn> getAll();
    InvoiceIn getById(Long id);
    InvoiceIn create(InvoiceInRequest request, Company company, Partner partner, Contract contract, Warehouse warehouse, Map<Long, Product> productsMap);
    InvoiceIn update(Long id, InvoiceInRequest request, Company company, Partner partner, Contract contract, Warehouse warehouse, Map<Long, Product> productsMap);
    void delete(Long id);
    InvoiceIn toggleDeleteStatus(Long id);
    InvoiceIn togglePostStatus(Long id);
}