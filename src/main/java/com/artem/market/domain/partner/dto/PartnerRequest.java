package com.artem.market.domain.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PartnerRequest {

    @NotBlank(message = "Код контрагента не может быть пустым")
    @Size(max = 10, message = "Длина кода не должна превышать 10 символов")
    private String code;

    @NotBlank(message = "Наименование контрагента не может быть пустым")
    @Size(max = 50, message = "Длина наименования не должна превышать 50 символов")
    private String name;

    private Boolean group;
    private Boolean deleted;

    private Long parentId;
}