package com.artem.market.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest{

    @NotBlank(message = "Product code cannot be empty")
    @Size(max = 10, message = "Code length must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Product name cannot be empty")
    @Size(max = 50, message = "Name length must not exceed 50 characters")
    private String name;

    private Boolean group;
    private Boolean deleted;
    private Long parentId;
}