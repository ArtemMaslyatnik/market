package com.artem.market.repository.document;

import com.artem.market.domain.invoiceout.entity.InvoiceOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceOutRepository extends JpaRepository<InvoiceOut, Long> {
}
