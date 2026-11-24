package com.artem.market.domain.invoiceout.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceOutResponse {
    private Long id;
    private String number;
    private LocalDateTime date;
    private boolean posted;
    private boolean deleted;
    private Long companyId;
    private String companyName;
    private Long partnerId;
    private String partnerName;
    private Long contractId;
    private String contractName;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal totalAmount;

    private List<InvoiceOutItemResponse> items;
}