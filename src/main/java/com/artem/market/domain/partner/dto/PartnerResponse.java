package com.artem.market.domain.partner.dto;

import com.artem.market.domain.contract.dto.ContractResponse;
import lombok.Data;

import java.util.List;


@Data
public class PartnerResponse {
    private Long id;
    private String code;
    private String name;
    private boolean group;
    private boolean deleted;
    private Long parentId;
    private String parentName;

    private List<ContractResponse> contracts;
}