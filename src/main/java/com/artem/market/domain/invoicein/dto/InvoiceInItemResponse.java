package com.artem.market.domain.invoicein.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceInItemResponse {

    private Long id;
    private Integer lineNumber;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal amount;
}
