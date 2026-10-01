package com.artem.market.domain.partner.mapper;

import com.artem.market.domain.partner.dto.PartnerRequest;
import com.artem.market.domain.partner.dto.PartnerResponse;
import com.artem.market.domain.partner.entity.Partner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) // Делает маппер Spring-бином
public interface PartnerMapper {

    // Из Entity в Response DTO
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    PartnerResponse toResponse(Partner entity);

    // Из Request DTO в Entity
    @Mapping(source = "parentId", target = "parent.id")
    Partner toEntity(PartnerRequest request);
}
