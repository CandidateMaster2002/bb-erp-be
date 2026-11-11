package com.boltblazers.erp.invoices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByOrderByCreatedAtDesc();
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
