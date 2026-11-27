package com.artem.market.domain.stocktransfer.service;

import com.artem.market.domain.company.entity.Company;
import com.artem.market.domain.stocktransfer.dto.StockTransferItemRequest;
import com.artem.market.domain.stocktransfer.dto.StockTransferRequest;
import com.artem.market.domain.stocktransfer.entity.StockTransfer;
import com.artem.market.domain.stocktransfer.entity.StockTransferItem;
import com.artem.market.domain.stocktransfer.mapper.StockTransferMapper;
import com.artem.market.domain.stocktransfer.repository.StockTransferRepository;
import com.artem.market.domain.product.entity.Product;
import com.artem.market.domain.warehouse.entity.Warehouse;
import com.artem.market.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockTransferServiceImpl implements StockTransferService {

    private final StockTransferRepository repository;
    private final StockTransferMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<StockTransfer> getAll() { return repository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public StockTransfer getById(Long id) {
        return findStockTransfer(id);
    }

    @Override
    @Transactional
    public StockTransfer create(StockTransferRequest request,
                            Company company,
                            Warehouse sourceWarehouse,
                            Warehouse destinationWarehouse,
                            Map<Long, Product> productsMap) {

        StockTransfer stockTransfer = mapper.toEntity(request);
        stockTransfer.setCompany(company);
        stockTransfer.setSourceWarehouse(sourceWarehouse);
        stockTransfer.setDestinationWarehouse(destinationWarehouse);

        if (request.getItems() != null) {
            populateItems(stockTransfer, request.getItems(), productsMap);
        }


        return repository.save(stockTransfer);
    }

    @Override
    @Transactional
    public StockTransfer update(Long id, StockTransferRequest request,
                            Company company,
                            Warehouse sourceWarehouse,
                            Warehouse destinationWarehouse
            ,
                            Map<Long, Product> productsMap) {
        StockTransfer existing = findStockTransfer(id);

        boolean targetIsDeleted = request.getDeleted() != null
                ? request.getDeleted()
                : existing.isDeleted();

        mapper.updateEntityFromRequest(request, existing);
        existing.setCompany(company);
        existing.setSourceWarehouse(sourceWarehouse);
        existing.setDestinationWarehouse(destinationWarehouse);
        existing.setDeleted(targetIsDeleted);


        if (request.getItems() != null) {
            existing.clearItems();
            populateItems(existing, request.getItems(), productsMap);
        }

        return repository.save(existing);
    }

    @Override
    @Transactional
    public StockTransfer toggleDeleteStatus(Long id) {
        StockTransfer stockTransfer = findStockTransfer(id);
        stockTransfer.setDeleted(!stockTransfer.isDeleted());

        return repository.save(stockTransfer);
    }

    @Override
    @Transactional
    public StockTransfer togglePostStatus(Long id) {
        StockTransfer stockTransfer = findStockTransfer(id);
        stockTransfer.setPosted(!stockTransfer.isPosted());
        return repository.save(stockTransfer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        StockTransfer stockTransfer = findStockTransfer(id);
        repository.delete(stockTransfer);
    }

    private StockTransfer findStockTransfer(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException("Документ не найден"));
    }

    private void populateItems(StockTransfer stockTransfer,
                               List<StockTransferItemRequest> itemRequests,
                               Map<Long, Product> productsMap) {
        int lineNum = 1;
        for (StockTransferItemRequest itemReq : itemRequests) {
            Product product = productsMap.get(itemReq.getProductId());
            if (product == null) {
                throw new BusinessRuleException("Товар с ID " + itemReq.getProductId() + " не найден");
            }

            StockTransferItem item = mapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setLineNumber(lineNum++);

            stockTransfer.addItem(item);
        }
    }
}


