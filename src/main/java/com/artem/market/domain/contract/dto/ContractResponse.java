package com.artem.market.domain.contract.dto;


import java.time.LocalDate;
import lombok.Data;

@Data
public class ContractResponse {

    private Long id;
    private String code;
    private String name;
    private LocalDate contractDate;
    private Long companyId;
    private String companyName;
    private Long partnerId;
    private String partnerName;
    private boolean deleted;
}
