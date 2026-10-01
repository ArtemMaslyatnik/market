package com.artem.market.repository.document;

import com.artem.market.model.document.InvoiceOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceOutRepository extends JpaRepository<InvoiceOut, Long> {
}
