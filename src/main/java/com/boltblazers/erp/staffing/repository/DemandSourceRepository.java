package com.boltblazers.erp.staffing.repository;

import com.boltblazers.erp.staffing.entity.DemandSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandSourceRepository extends JpaRepository<DemandSource, Long> {
}
