package com.boltblazers.erp.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubActionRepository extends JpaRepository<SubAction, Long> {
    List<SubAction> findByTask(Task task);
}
