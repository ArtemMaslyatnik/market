package com.artem.market.domain.invoiceout.dto;


import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceOutItemRequest {

    private Long productId;
    private Integer lineNumber;
    @PositiveOrZero(message = "Quantity cannot be negative")
    private BigDecimal quantity;
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "Amount cannot be negative")
    private BigDecimal amount;
}
