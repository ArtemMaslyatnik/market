package com.artem.market.domain.invoiceout.mapper;

import com.artem.market.domain.invoiceout.dto.InvoiceOutItemRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutItemResponse;
import com.artem.market.domain.invoiceout.dto.InvoiceOutRequest;
import com.artem.market.domain.invoiceout.dto.InvoiceOutResponse;
import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import com.artem.market.domain.invoiceout.entity.InvoiceOutItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InvoiceOutMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "partnerId", source = "partner.id")
    @Mapping(target = "partnerName", source = "partner.name")
    @Mapping(target = "contractId", source = "contract.id")
    @Mapping(target = "contractName", source = "contract.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    InvoiceOutResponse toResponse(InvoiceOut entity);

    List<InvoiceOutResponse> toResponseList(List<InvoiceOut> invoices);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    InvoiceOutItemResponse toItemResponse(InvoiceOutItem item);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "partner", ignore = true)
    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "posted", constant = "false")
    @Mapping(target = "deleted", defaultValue = "false")
    @Mapping(target = "totalAmount", defaultValue = "0")
    InvoiceOut toEntity(InvoiceOutRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "lineNumber", ignore = true)
    @Mapping(target = "price", defaultValue = "0")
    @Mapping(target = "quantity", defaultValue = "0")
    @Mapping(target = "amount", defaultValue = "0")
    InvoiceOutItem toItemEntity(InvoiceOutItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "posted", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "partner", ignore = true)
    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntityFromRequest(InvoiceOutRequest request, @MappingTarget InvoiceOut invoice);
}
