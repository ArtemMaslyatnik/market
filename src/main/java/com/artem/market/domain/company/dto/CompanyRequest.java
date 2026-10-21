package com.artem.market.domain.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequest {

    @NotBlank(message = "Company code cannot be empty")
    @Size(max = 10, message = "Code length must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Company name cannot be empty")
    @Size(max = 50, message = "Name length must not exceed 50 characters")
    private String name;

    private Boolean deleted;
}