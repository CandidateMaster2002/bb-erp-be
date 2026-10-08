package com.boltblazers.erp.staffing.repository;

import com.boltblazers.erp.staffing.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findByDemandSourceId(Long demandSourceId);
}
