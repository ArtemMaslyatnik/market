package com.artem.market.domain.partner.service;

import com.artem.market.domain.partner.entity.Partner;
import java.util.List;

public interface PartnerService {
    List<Partner> getAll();
    Partner getById(Long id);
    Partner create(Partner entity);
    Partner update(Long id, Partner entity);
    Partner delete(Long id);
}