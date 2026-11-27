package com.artem.market.domain.stocktransfer.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StockTransferResponse {
    private Long id;
    private String number;
    private LocalDateTime date;
    private boolean posted;
    private boolean deleted;
    private Long companyId;
    private String companyName;
    private Long sourceWarehouseId;
    private String sourceWarehouseName;
    private Long destinationWarehouseId;
    private String destinationWarehouseName;

    private List<StockTransferItemResponse> items;
}