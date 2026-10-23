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
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "partner", ignore = true)
    Contract toEntity(ContractRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "partnerId", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ContractRequest request,
                                 @MappingTarget Contract contract
    );

}
