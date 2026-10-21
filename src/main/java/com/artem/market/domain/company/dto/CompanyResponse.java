package com.artem.market.domain.company.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CompanyResponse {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;
}