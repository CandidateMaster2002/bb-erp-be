package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitmentRepository extends JpaRepository<Commitment, Long> {
}
