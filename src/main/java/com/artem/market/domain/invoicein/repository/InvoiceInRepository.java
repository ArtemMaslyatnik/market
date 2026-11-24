package com.artem.market.domain.invoicein.repository;

import com.artem.market.domain.invoicein.entity.InvoiceIn;
import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceInRepository extends JpaRepository<InvoiceIn, Long> {
    @Override
    @EntityGraph(attributePaths = {"company", "partner", "contract", "warehouse"})
    List<InvoiceIn> findAll();

    @Override
    @EntityGraph(attributePaths = {"company", "partner", "contract", "warehouse", "items", "items.product"})
    Optional<InvoiceIn> findById(Long id);
}