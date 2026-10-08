package com.boltblazers.erp.staffing.repository;

import com.boltblazers.erp.staffing.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findBySupplySourceId(Long supplySourceId);
}
