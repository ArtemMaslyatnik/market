package com.artem.market.repository.document;

import com.artem.market.model.document.InvoiceIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInRepository extends JpaRepository<InvoiceIn, Long> {
}
