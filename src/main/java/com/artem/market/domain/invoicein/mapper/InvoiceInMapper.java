package com.artem.market.domain.invoicein.mapper;

import com.artem.market.domain.invoicein.dto.InvoiceInItemRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInItemResponse;
import com.artem.market.domain.invoicein.dto.InvoiceInRequest;
import com.artem.market.domain.invoicein.dto.InvoiceInResponse;
import com.artem.market.domain.invoicein.entity.InvoiceIn;
import com.artem.market.domain.invoicein.entity.InvoiceInItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InvoiceInMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "partnerId", source = "partner.id")
    @Mapping(target = "partnerName", source = "partner.name")
    @Mapping(target = "contractId", source = "contract.id")
    @Mapping(target = "contractName", source = "contract.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    InvoiceInResponse toResponse(InvoiceIn entity);

    List<InvoiceInResponse> toResponseList(List<InvoiceIn> invoices);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    InvoiceInItemResponse toItemResponse(InvoiceInItem item);



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
    InvoiceIn toEntity(InvoiceInRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "lineNumber", ignore = true)
    @Mapping(target = "price", defaultValue = "0")
    @Mapping(target = "quantity", defaultValue = "0")
    @Mapping(target = "amount", defaultValue = "0")
    InvoiceInItem toItemEntity(InvoiceInItemRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "posted", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "partner", ignore = true)
    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntityFromRequest(InvoiceInRequest request, @MappingTarget InvoiceIn invoice);
}
