package com.artem.market.domain.partner.mapper;

import com.artem.market.domain.contract.mapper.ContractMapper;
import com.artem.market.domain.partner.dto.PartnerRequest;
import com.artem.market.domain.partner.dto.PartnerResponse;
import com.artem.market.domain.partner.entity.Partner;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ContractMapper.class}
)
public interface PartnerMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    PartnerResponse toResponse(Partner entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "contracts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Partner toEntity( PartnerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "contracts", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(
            PartnerRequest request,
            @MappingTarget Partner existingEntity
    );
}
