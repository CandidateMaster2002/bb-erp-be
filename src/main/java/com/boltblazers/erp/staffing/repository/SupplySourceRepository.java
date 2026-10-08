package com.boltblazers.erp.staffing.repository;

import com.boltblazers.erp.staffing.entity.SupplySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplySourceRepository extends JpaRepository<SupplySource, Long> {
}
