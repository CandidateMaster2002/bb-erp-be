package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadLinkRepository extends JpaRepository<LeadLink, Long> {
    List<LeadLink> findByLeadIdOrderByCreatedAtDesc(Long leadId);
}
