package com.artem.market.domain.partner.mapper;

import com.artem.market.domain.partner.dto.PartnerRequest;
import com.artem.market.domain.partner.dto.PartnerResponse;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.model.base.context.JpaContext;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartnerMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    PartnerResponse toResponse(Partner entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "parentId", target = "parent")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Partner toEntity( PartnerRequest request, @Context JpaContext context );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "parentId", target = "parent.id")
    void updateEntityFromRequest(
            PartnerRequest request,
            @MappingTarget Partner existingEntity,
            @Context JpaContext context );

    default Partner mapIdToEntity( Long id, @Context JpaContext context ) {
        return context.resolveEntity(id, Partner.class);
    }
}
