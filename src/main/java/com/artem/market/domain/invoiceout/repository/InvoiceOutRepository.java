package com.artem.market.domain.invoiceout.repository;

import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceOutRepository extends JpaRepository<InvoiceOut, Long> {
    @Override
    @EntityGraph(attributePaths = {"company", "partner", "contract", "warehouse"})
    List<InvoiceOut> findAll();

    @Override
    @EntityGraph(attributePaths = {"company", "partner", "contract", "warehouse", "items", "items.product"})
    Optional<InvoiceOut> findById(Long id);
}