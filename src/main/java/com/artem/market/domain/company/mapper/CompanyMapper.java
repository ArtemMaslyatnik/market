package com.artem.market.domain.company.mapper;

import com.artem.market.domain.company.dto.CompanyRequest;
import com.artem.market.domain.company.dto.CompanyResponse;
import com.artem.market.domain.company.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE
)
public interface CompanyMapper {

    // 1. Из Сущности в Response DTO
    CompanyResponse toResponse(Company entity);

    // 2. Из Request DTO в новую Сущность (для POST / create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Company toEntity(CompanyRequest request);

    // 3. Частичное обновление существующей сущности из БД (для PUT / update)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CompanyRequest request,
                                 @MappingTarget Company existingEntity);

}