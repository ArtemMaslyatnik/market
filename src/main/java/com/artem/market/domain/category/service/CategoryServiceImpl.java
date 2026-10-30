package com.artem.market.domain.category.service;

import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.category.dto.CategoryRequest;
import com.artem.market.domain.category.entity.Category;
import com.artem.market.domain.category.mapper.CategoryMapper;
import com.artem.market.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return findCategory(id);
    }

    @Override
    @Transactional
    public Category create(CategoryRequest  request) {
        Category parent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());
        Category category = mapper.toEntity(request);
        category.setParent(parent);
        return repository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category existing = findCategory(id);
        Category newParent = request.getParentId() == null
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

        if (!existing.isDeleted() && existing.isGroup() && targetIsDeleted) {
            validateGroupDeletion(existing);
        }

        mapper.updateEntityFromRequest(request, existing);

        existing.setGroup(targetIsGroup);
        existing.setDeleted(targetIsDeleted);
        existing.setParent(newParent);

        return repository.save(existing);
    }

    @Override
    @Transactional
    public Category toggleDeleteStatus(Long id) {
        Category category = findCategory(id);

        if (!category.isDeleted() && category.isGroup()) {
            validateGroupDeletion(category);
        }
        category.setDeleted(!category.isDeleted());

        return repository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category existing = findCategory(id);
        repository.delete(existing);
    }

    private Category findCategory(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Компания не найдена"));
    }

    private Category findValidParent(Long parentId) {
        Category parent = repository.findById(parentId)
                .orElseThrow(() ->
                        new BusinessRuleException("Группа не найдена"));
        if (!parent.isGroup()) {
            throw new BusinessRuleException( "Родитель должен быть группой" );
        }
        return parent;
    }

    private boolean isDescendantOrSelf(Category candidate, Long categoryId) {
        Category current = candidate;
        while (current != null) {
            if (categoryId.equals(current.getId())) {
                return true;
            }
            current = current.getParent();
        } return false;
    }

    private void validateGroupDeletion(Category category) {
        if (category.isGroup() && repository.existsByParentId(category.getId())) {
            throw new BusinessRuleException(
                    "Нельзя пометить группу на удаление, пока в ней содержатся дочерние категории"
            );
        }
    }

}