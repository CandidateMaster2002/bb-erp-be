package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeadLogRepository extends JpaRepository<LeadLog, Long> {
    List<LeadLog> findByLeadIdOrderByCreatedAtDesc(Long leadId);

    // Pending actions for a specific date
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate = :date
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findPendingActionsForDate(@Param("date") LocalDate date);

    // Overdue + today's pending actions
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate <= :date
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findOverdueAndTodayPendingActions(@Param("date") LocalDate date);

    // Pending actions in a date range
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate BETWEEN :from AND :to
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findPendingActionsBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
