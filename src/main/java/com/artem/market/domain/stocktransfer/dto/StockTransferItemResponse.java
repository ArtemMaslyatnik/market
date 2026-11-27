package com.artem.market.domain.stocktransfer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockTransferItemResponse {

    private Long id;
    private Integer lineNumber;
    private Long productId;
    private String productName;
    private BigDecimal quantity;

}
