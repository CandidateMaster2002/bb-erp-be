package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LeadLogRepository extends JpaRepository<LeadLog, Long> {
    List<LeadLog> findByLeadIdOrderByCreatedAtDesc(Long leadId);

    // Pending actions for a specific date (between start and end of that date)
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate >= :start AND l.nextActionDate < :end
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findPendingActionsForDateRange(@Param("start") Instant start, @Param("end") Instant end);

    // Overdue + today's pending actions
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate <= :end
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findOverdueAndTodayPendingActions(@Param("end") Instant end);

    // Pending actions in a date range
    @Query("""
        SELECT l FROM LeadLog l
        WHERE l.nextAction IS NOT NULL
          AND l.nextActionDate >= :start AND l.nextActionDate < :end
          AND l.actionStatus = 'PENDING'
        ORDER BY l.nextActionDate ASC
    """)
    List<LeadLog> findPendingActionsBetween(@Param("start") Instant start, @Param("end") Instant end);
}
