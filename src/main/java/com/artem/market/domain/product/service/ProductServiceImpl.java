package com.artem.market.domain.product.service;

import com.artem.market.exception.BusinessRuleException;
import com.artem.market.domain.product.dto.ProductRequest;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.product.mapper.ProductMapper;
import com.artem.market.domain.product.repository.ProductRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() { return repository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return findProduct(id);
    }

    @Override
    @Transactional
    public Product create(ProductRequest request) {
        Product parent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());

        Product product = mapper.toEntity(request);
        product.setParent(parent);

        return repository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product existing = findProduct(id);
        Product newParent = request.getParentId() == null
                ? null
                : findValidParent(request.getParentId());
        boolean targetIsGroup = request.getGroup()
                != null ? request.getGroup()
                : existing.isGroup();
        boolean targetIsDeleted = request.getDeleted() != null
                ? request.getDeleted()
                : existing.isDeleted();
        // Нельзя назначить контрагента родителем самому себе или одному из его потомков.
        if (newParent != null && isDescendantOrSelf(newParent, id)) {
            throw new BusinessRuleException( "Нельзя назначить родителем самого себя или дочерний элемент" );
        }
        // Группа не может стать элементом.
        if (existing.isGroup() && !targetIsGroup) {
            throw new BusinessRuleException("Нельзя изменить группу на элемент");
        }
        if (!existing.isGroup() && targetIsGroup) {
            throw new BusinessRuleException("Запрещено изменять элемент на группу");
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
    public Product toggleDeleteStatus(Long id) {
        Product product = findProduct(id);

        if (!product.isDeleted() && product.isGroup()) {
            validateGroupDeletion(product);
        }

        product.setDeleted(!product.isDeleted());

        return repository.save(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = findProduct(id);

        if (product.isGroup() && repository.existsByParentId(id)) {
            throw new BusinessRuleException( "Нельзя полностью удалить группу, пока в ней содержатся дочерние товары" );
        }
        repository.delete(product);
    }

    private Product findProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Элемент не найден"));
    }

    public Product findValidParent(Long parentId) {
        Product parent = repository.findById(parentId)
                .orElseThrow(() -> new BusinessRuleException("Группа не найдена"));
        if (!parent.isGroup()) {
            throw new BusinessRuleException( "Родитель должен быть группой" );
        }
        return parent;
    }

    private boolean isDescendantOrSelf(Product candidate, Long productId) {
        Product current = candidate;

        while (current != null) {
            if (productId.equals(current.getId())) {
                return true;
            }
            current = current.getParent();
        } return false;
    }

    private void validateGroupDeletion(Product product) {
        if (repository.existsByParentId(product.getId())) {
            throw new BusinessRuleException(
                    "Нельзя пометить группу на удаление, пока в ней содержатся дочерние товары"
            );
        }
    }
}
