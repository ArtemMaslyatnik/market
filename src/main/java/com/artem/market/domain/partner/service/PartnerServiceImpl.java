package com.artem.market.domain.partner.service;

import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.partner.entity.Partner;
import com.artem.market.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Partner> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Partner getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Контрагент не найден"));
    }

    @Override
    @Transactional
    public Partner create(Partner entity) {
        // ЗАЩИТА: Если при создании указан родитель, проверяем его существование и тип
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            Partner parent = repository.findById(entity.getParent().getId())
                    .orElseThrow(() -> new BusinessRuleException("Указанная родительская группа не найдена"));

            if (Boolean.FALSE.equals(parent.getIsGroup())) {
                throw new BusinessRuleException("Родительский элемент должен быть группой (isGroup = true)");
            }
        }
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Partner update(Long id, Partner entity) {
        Partner existing = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Контрагент не найден"));

        // Защита от null при частичном обновлении флагов
        boolean targetIsGroup = entity.getIsGroup() != null ? entity.getIsGroup() : existing.getIsGroup();
        boolean targetIsDeleted = entity.getIsDeleted() != null ? entity.getIsDeleted() : existing.getIsDeleted();

        // ЗАЩИТА: Циклическая ссылка
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            if (entity.getParent().getId().equals(id)) {
                throw new BusinessRuleException("Контрагент не может быть родителем самого себя");
            }

            Partner newParent = repository.findById(entity.getParent().getId())
                    .orElseThrow(() -> new BusinessRuleException("Указанная родительская группа не найдена"));

            if (Boolean.FALSE.equals(newParent.getIsGroup())) {
                throw new BusinessRuleException("Родительский элемент должен быть группой (isGroup = true)");
            }
            existing.setParent(newParent);
        } else if (entity.getParent() == null) {
            existing.setParent(null);
        }

        // ЗАЩИТА: Из группы (true) в элемент (false)
        if (Boolean.TRUE.equals(existing.getIsGroup()) && !targetIsGroup) {
            if (repository.existsByParentId(id)) {
                throw new BusinessRuleException("Нельзя изменить тип группы на 'Элемент', так как она содержит дочерние элементы");
            }
        }

        // ЗАЩИТА: Безусловный запрет превращения элемента (false) в группу (true)
        if (Boolean.FALSE.equals(existing.getIsGroup()) && targetIsGroup) {
            throw new BusinessRuleException("Категорически запрещено изменять обычный элемент (контрагента) на группу (папку)");
        }

        // ЗАЩИТА: Удаление непустой группы
        if (Boolean.TRUE.equals(existing.getIsGroup()) && targetIsDeleted) {
            if (repository.existsByParentId(id)) {
                throw new BusinessRuleException("Нельзя пометить группу как удаленную, пока в ней содержатся дочерние элементы");
            }
        }

        existing.setName(entity.getName());
        existing.setCode(entity.getCode());
        existing.setIsGroup(targetIsGroup);
        existing.setIsDeleted(targetIsDeleted);

        return repository.save(existing);
    }

    @Override
    @Transactional
    public Partner delete(Long id) {
        Partner existing = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Контрагент не найден"));

        if (Boolean.TRUE.equals(existing.getIsGroup())) {
            if (repository.existsByParentId(id)) {
                throw new BusinessRuleException("Нельзя пометить на удаление группу, пока в ней содержатся дочерние элементы");
            }
        }

        existing.setIsDeleted(true);
        return repository.save(existing);
    }
}