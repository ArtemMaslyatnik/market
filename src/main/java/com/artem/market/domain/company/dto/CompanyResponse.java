package com.artem.market.domain.company.dto;

import lombok.Data;


@Data
public class CompanyResponse {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;
}