package com.artem.market.domain.partner.dto;

import lombok.Data;


@Data
public class PartnerResponse {

    private Long id;
    private String code;
    private String name;
    private boolean group;
    private boolean deleted;
    private Long parentId;
    private String parentName;
}