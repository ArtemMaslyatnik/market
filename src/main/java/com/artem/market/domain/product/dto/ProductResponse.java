package com.artem.market.domain.product.dto;

import com.artem.market.domain.category.dto.CategoryResponse;
import lombok.Data;

import java.util.List;


@Data
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private boolean group;
    private boolean deleted;
    private Long parentId;
    private String parentName;

    private List<CategoryResponse> categories;
}