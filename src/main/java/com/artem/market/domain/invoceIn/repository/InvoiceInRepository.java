package com.artem.market.domain.invoceIn.repository;

import com.artem.market.domain.invoceIn.entity.InvoiceIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInRepository extends JpaRepository<InvoiceIn, Long> {
}
