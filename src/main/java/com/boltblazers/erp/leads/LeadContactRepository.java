package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadContactRepository extends JpaRepository<LeadContact, Long> {
    Optional<LeadContact> findByValue(String value);
    java.util.List<LeadContact> findByLead(Lead lead);
}
