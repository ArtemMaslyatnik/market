package com.artem.market.domain.stocktransfer.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StockTransferRequest {

    @NotBlank(message = "Document number cannot be empty")
    @Size(max = 10, message = "Number length must not exceed 10 characters")
    private String number;
    @NotNull(message = "Document date cannot be empty")
    private LocalDateTime date;

    private Long companyId;
    private Long sourceWarehouseId;
    private Long destinationWarehouseId;


    private Boolean posted;
    private Boolean deleted;

    @Valid
    private List<StockTransferItemRequest> items;
}