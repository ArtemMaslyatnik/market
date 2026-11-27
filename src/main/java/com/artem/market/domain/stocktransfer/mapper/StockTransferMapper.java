package com.artem.market.domain.stocktransfer.mapper;

import com.artem.market.domain.stocktransfer.dto.StockTransferRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferItemResponse;
import com.artem.market.domain.stocktransfer.dto.StockTransferItemRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferResponse;
import com.artem.market.domain.stocktransfer.entity.StockTransfer;
import com.artem.market.domain.stocktransfer.entity.StockTransferItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockTransferMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "sourceWarehouseId", source = "sourceWarehouse.id")
    @Mapping(target = "sourceWarehouseName", source = "sourceWarehouse.name")
    @Mapping(target = "destinationWarehouseId", source = "destinationWarehouse.id")
    @Mapping(target = "destinationWarehouseName", source = "destinationWarehouse.name")
    StockTransferResponse toResponse(StockTransfer entity);

    List<StockTransferResponse> toResponseList(List<StockTransfer> invoices);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    StockTransferItemResponse toItemResponse(StockTransferItem item);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "sourceWarehouse", ignore = true)
    @Mapping(target = "destinationWarehouse", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "posted", constant = "false")
    @Mapping(target = "deleted", defaultValue = "false")
    StockTransfer toEntity(StockTransferRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transfer", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "lineNumber", ignore = true)
    @Mapping(target = "quantity", defaultValue = "0")
    StockTransferItem toItemEntity(StockTransferItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "posted", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "sourceWarehouse", ignore = true)
    @Mapping(target = "destinationWarehouse", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntityFromRequest(StockTransferRequest request, @MappingTarget StockTransfer invoice);
}
