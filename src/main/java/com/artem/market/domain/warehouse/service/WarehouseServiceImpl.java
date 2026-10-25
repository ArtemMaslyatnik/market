package com.artem.market.domain.warehouse.service;

import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.warehouse.dto.WarehouseRequest;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.domain.warehouse.mapper.WarehouseMapper;
import com.artem.market.domain.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<Warehouse> getAll() { return repository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Warehouse getById(Long id) { return findWarehouse(id); }

    @Override
    @Transactional
    public Warehouse create(WarehouseRequest request) {
        Warehouse parent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());

        Warehouse warehouse = mapper.toEntity(request);
        warehouse.setParent(parent);
        return repository.save(warehouse);
    }

    @Override
    @Transactional
    public Warehouse update(Long id, WarehouseRequest request) {
        Warehouse existing = findWarehouse(id);
        Warehouse newParent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());

        boolean targetIsGroup = request.getGroup() != null
                ? request.getGroup()
                : existing.isGroup();
        boolean targetIsDeleted = request.getDeleted() != null
                ? request.getDeleted()
                : existing.isDeleted();

        // Нельзя назначить склад родителем самому себе // или одному из его потомков.
        if (newParent != null && isDescendantOrSelf(newParent, id)) {
            throw new BusinessRuleException( "Нельзя назначить родителем самого себя или дочерний элемент" );
        }

        // Группа  не может стать элементом.
        if (existing.isGroup() && !targetIsGroup) {
            throw new BusinessRuleException("Нельзя изменить группу на элемент");
        }

        if (!existing.isGroup() && targetIsGroup) {
            throw new BusinessRuleException( "Запрещено изменять элемент на группу" );
        }

        if (existing.isGroup() && targetIsDeleted
                && repository.existsByParentId(id)) {
            throw new BusinessRuleException( "Нельзя пометить на удаление группу, пока в ней содержатся активные элементы" );
        }

        mapper.updateEntityFromRequest(request, existing);

        existing.setGroup(targetIsGroup);
        existing.setDeleted(targetIsDeleted);
        existing.setParent(newParent);

        return repository.save(existing);
    }

    @Override
    @Transactional
    public Warehouse delete(Long id) {
        Warehouse warehouse = findWarehouse(id);

        if (warehouse.isGroup() && repository.existsByParentId(id)) {
            throw new BusinessRuleException("Нельзя пометить на удаление группу, пока в ней содержатся активные элементы");
        }
        warehouse.setDeleted(true);
        return repository.save(warehouse);
    }

    private Warehouse findWarehouse(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Элемент не найден"));
    }

    private Warehouse findValidParent(Long parentId) {
        Warehouse parent = repository.findById(parentId)
                .orElseThrow(() ->
                        new BusinessRuleException("Группа не найдена"));
        if (!parent.isGroup()) {
            throw new BusinessRuleException( "Родитель должен быть группой" );
        }
        return parent;
    }

    private boolean isDescendantOrSelf(Warehouse candidate, Long warehouseId) {
        Warehouse current = candidate;
        while (current != null) {
            if (warehouseId.equals(current.getId())) {
                return true;
            }
            current = current.getParent();
        } return false;
    }
}