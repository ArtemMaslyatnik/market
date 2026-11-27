package com.artem.market.domain.stocktransfer.dto;


import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockTransferItemRequest {

    private Long productId;
    private Integer lineNumber;
    @PositiveOrZero(message = "Quantity cannot be negative")
    private BigDecimal quantity;
}
