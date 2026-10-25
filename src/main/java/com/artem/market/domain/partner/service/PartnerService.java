package com.artem.market.domain.partner.service;

import com.artem.market.domain.partner.dto.PartnerRequest;
import com.artem.market.domain.partner.entity.Partner;
import java.util.List;

public interface PartnerService {
    List<Partner> getAll();
    Partner getById(Long id);
    Partner create(PartnerRequest request);
    Partner update(Long id, PartnerRequest request);
    Partner delete(Long id);
}