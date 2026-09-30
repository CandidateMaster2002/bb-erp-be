package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowupRepository extends JpaRepository<Followup, Long> {
}
