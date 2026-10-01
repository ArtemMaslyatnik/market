package com.artem.market.controller.catalog;

import com.artem.market.model.catalog.Product;
import com.artem.market.repository.catalog.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "1. Справочник Товаров (Номенклатура)")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Получить список всех товаров и групп")
    public List<Product> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public Product getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый товар или группу")
    public Product create(@RequestBody Product entity) {
        // 1. ЗАЩИТА: Если при создании указан родитель, проверяем его существование и тип
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            Product parent = repository.findById(entity.getParent().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Указанная родительская группа не найдена"));

            if (Boolean.FALSE.equals(parent.getIsGroup())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Родительский элемент должен быть группой (isGroup = true)");
            }
        }

        return repository.save(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий товар или группу")
    public Product update(@PathVariable Long id, @RequestBody Product entity) {
        // 1. Проверяем, существует ли обновляемый товар
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        // Вычисляем целевые значения флагов с защитой от null (если фронтенд не передал поле, берем старое из базы)
        boolean targetIsGroup = entity.getIsGroup() != null ? entity.getIsGroup() : existing.getIsGroup();
        boolean targetIsDeleted = entity.getIsDeleted() != null ? entity.getIsDeleted() : existing.getIsDeleted();

        // 2. ЗАЩИТА: Проверка циклической ссылки (нельзя назначить родителем самого себя)
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            if (entity.getParent().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товар не может быть родителем самого себя");
            }

            Product newParent = repository.findById(entity.getParent().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Указанная родительская группа не найдена"));

            if (Boolean.FALSE.equals(newParent.getIsGroup())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Родительский элемент должен быть группой (isGroup = true)");
            }
            existing.setParent(newParent);
        } else if (entity.getParent() == null) {
            // Если в запросе parent явно пришел как null, выносим товар на верхний уровень дерева
            existing.setParent(null);
        }

        // 3. ЗАЩИТА: Если была группа (true), а её меняют на элемент (false)
        if (Boolean.TRUE.equals(existing.getIsGroup()) && !targetIsGroup) {
            boolean hasChildren = repository.existsByParentId(id);
            if (hasChildren) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Нельзя изменить тип группы на 'Товар', так как она содержит дочерние элементы");
            }
        }

        // 4. ЗАЩИТА: Безусловный запрет превращения товара (false) в группу (true)
        if (Boolean.FALSE.equals(existing.getIsGroup()) && targetIsGroup) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Категорически запрещено изменять обычный элемент (товар) на группу (папку)");
        }

        // 5. ЗАЩИТА: Запрет пометки на удаление непустой группы при обновлении
        if (Boolean.TRUE.equals(existing.getIsGroup()) && targetIsDeleted) {
            boolean hasChildren = repository.existsByParentId(id);
            if (hasChildren) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Нельзя пометить группу как удаленную, пока в ней содержатся дочерние элементы");
            }
        }

        // 6. Обновляем поля
        existing.setName(entity.getName());
        existing.setCode(entity.getCode());
        existing.setIsGroup(targetIsGroup);
        existing.setIsDeleted(targetIsDeleted);

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Возвращаем 200 OK при мягком логическом удалении
    @Operation(summary = "Пометить товар или группу на удаление (мягкое удаление)")
    public Product delete(@PathVariable Long id) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (Boolean.TRUE.equals(existing.getIsGroup())) {
            boolean hasChildren = repository.existsByParentId(id);
            if (hasChildren) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Нельзя пометить на удаление группу, пока в ней содержатся дочерние элементы");
            }
        }

        existing.setIsDeleted(true);

        return repository.save(existing);
    }
}