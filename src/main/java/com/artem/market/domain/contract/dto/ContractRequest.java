package com.artem.market.domain.contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractRequest {
    @NotBlank(message = "Contract code cannot be empty")
    @Size(max = 10, message = "Code length must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Contract name cannot be empty")
    @Size(max = 50, message = "Name length must not exceed 50 characters")
    private String name;

    @NotNull(message = "Contract date cannot be empty")
    private LocalDate contractDate;

    @NotNull(message = "Company cannot be empty")
    private Long companyId;

    @NotNull(message = "Partner cannot be empty")
    private Long partnerId;

    private Boolean deleted;

}
