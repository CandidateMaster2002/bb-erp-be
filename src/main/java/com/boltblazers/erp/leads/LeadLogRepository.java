package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeadLogRepository extends JpaRepository<LeadLog, Long> {
    List<LeadLog> findByLeadIdOrderByCreatedAtDesc(Long leadId);
    List<LeadLog> findByNextActionDateOrderByNextActionDateAsc(LocalDate date);
    List<LeadLog> findByNextActionDateBetweenOrderByNextActionDateAsc(LocalDate from, LocalDate to);
    List<LeadLog> findByNextActionDateLessThanEqualAndNextActionIsNotNullOrderByNextActionDateAsc(LocalDate date);
}
