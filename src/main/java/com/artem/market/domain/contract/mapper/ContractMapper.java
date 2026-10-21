package com.artem.market.domain.contract.mapper;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.contract.dto.ContractRequest;
import com.artem.market.domain.contract.dto.ContractResponse;
import com.artem.market.domain.contract.entity.Contract;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.model.base.context.JpaContext;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {

    @Mapping(target = "partnerId", source = "partner.id")
    @Mapping(target = "partnerName", source = "partner.name")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    ContractResponse toResponse(Contract contract);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "company", source = "companyId")
    @Mapping(target = "partner", source = "partnerId")
    Contract toEntity(ContractRequest request ,@Context JpaContext context);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "partnerId", target = "partner")
    @Mapping(source = "companyId", target = "company")
    void updateEntityFromRequest(ContractRequest request, @MappingTarget Contract contract, @Context JpaContext context);

    default Company mapCompanyIdToEntity(Long id, @Context JpaContext context) {
        return id == null ? null : context.resolveEntity(id, Company.class);
    }

    default Partner mapPartnerIdToEntity(Long id, @Context JpaContext context) {
        return id == null ? null : context.resolveEntity(id, Partner.class);
    }
}
