package com.artem.market.domain.invoicein.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceInRequest {

    @NotBlank(message = "Document number cannot be empty")
    @Size(max = 10, message = "Number length must not exceed 10 characters")
    private String number;
    @NotNull(message = "Document date cannot be empty")
    private LocalDateTime date;

    private Long companyId;
    private Long partnerId;
    private Long contractId;
    private Long warehouseId;

    @PositiveOrZero(message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    private Boolean posted;
    private Boolean deleted;

    @Valid
    private List<InvoiceInItemRequest> items;
}