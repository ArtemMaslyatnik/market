package com.artem.market.domain.partner.service;

import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.partner.dto.PartnerRequest; // Импорт DTO
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.mapper.PartnerMapper;   // ВНЕДРИЛИ МАППЕР
import com.artem.market.domain.partner.repository.PartnerRepository;
import com.artem.market.model.base.context.JpaContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository repository;
    private final PartnerMapper mapper;
    private final JpaContext jpaContext;

    @Override
    @Transactional(readOnly = true)
    public List<Partner> getAll() {

        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Partner getById(Long id) {
        return findPartner(id);
    }

    @Override
    @Transactional
    public Partner create(PartnerRequest request) {
        Partner partner = mapper.toEntity(request, jpaContext);

        Partner parent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());

        partner.setParent(parent);

        return repository.save(partner);
    }

    @Override @Transactional public Partner update(Long id, PartnerRequest request) {
        Partner existing = findPartner(id);
        Partner newParent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());
        boolean targetIsGroup = request.getIsGroup()
                != null ? request.getIsGroup()
                : existing.isGroup();
        boolean targetIsDeleted = request.getIsDeleted() != null
                ? request.getIsDeleted()
                : existing.isDeleted();
        // Нельзя назначить контрагента родителем самому себе или одному из его потомков.
        if (newParent != null && isDescendantOrSelf(newParent, id)) {
            throw new BusinessRuleException( "Нельзя назначить родителем самого себя или дочернего контрагента" );
        }
        // Группа не может стать элементом.
        if (existing.isGroup() && !targetIsGroup) {
            throw new BusinessRuleException( "Нельзя изменить группу на элемент" );
        }
        if (!existing.isGroup() && targetIsGroup) {
            throw new BusinessRuleException( "Запрещено изменять обычного контрагента на группу" );
        }
        // Накатываем изменения.
        mapper.updateEntityFromRequest(request, existing, jpaContext);
        existing.setGroup(targetIsGroup);
        existing.setDeleted(targetIsDeleted);
        existing.setParent(newParent);
        return repository.save(existing);
    }

    @Override @Transactional public Partner delete(Long id) {
        Partner partner = findPartner(id);
        if (partner.isGroup() && repository.existsByParentId(id)) {
            throw new BusinessRuleException( "Нельзя пометить на удаление группу, пока в ней содержатся элементы" );
        }
        partner.setDeleted(true);
        return repository.save(partner);
    }

    private Partner findPartner(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Контрагент не найден"));
    }

    private Partner findValidParent(Long parentId) {
        Partner parent = repository.findById(parentId)
                .orElseThrow(() -> new BusinessRuleException("Указанная родительская группа не найдена"));
        if (!parent.isGroup()) {
            throw new BusinessRuleException( "Родительский контрагент должен быть группой" );
        }
        return parent;
    }
    private boolean isDescendantOrSelf(Partner candidate, Long partnerId) {
        Partner current = candidate; while (current != null) {
            if (partnerId.equals(current.getId())) {
                return true;
            }
            current = current.getParent();
        } return false;
    }
}
