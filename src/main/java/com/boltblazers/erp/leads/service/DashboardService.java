package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.FollowupStatus;
import com.boltblazers.erp.leads.CommitmentStatus;
import com.boltblazers.erp.leads.dto.DashboardResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @PersistenceContext
    private EntityManager em;

    public DashboardResponse getDashboardMetrics() {
        DashboardResponse res = new DashboardResponse();
        Instant now = Instant.now();
        Instant startOfDay = now.truncatedTo(ChronoUnit.DAYS); // roughly UTC start of day
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        Instant startOfWeek = now.minus(7, ChronoUnit.DAYS);

        // Today Followups
        Long todayFollowups = em.createQuery(
                "SELECT COUNT(f) FROM Followup f WHERE f.status = :status AND f.dueAt >= :start AND f.dueAt < :end", Long.class)
                .setParameter("status", FollowupStatus.PENDING)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .getSingleResult();
        res.setTodayFollowups(todayFollowups != null ? todayFollowups : 0);

        // Overdue Followups
        Long overdueFollowups = em.createQuery(
                "SELECT COUNT(f) FROM Followup f WHERE f.status = :status AND f.dueAt < :start", Long.class)
                .setParameter("status", FollowupStatus.PENDING)
                .setParameter("start", startOfDay)
                .getSingleResult();
        res.setOverdueFollowups(overdueFollowups != null ? overdueFollowups : 0);

        // Pending Commitments
        Long pendingCommitments = em.createQuery(
                "SELECT COUNT(c) FROM Commitment c WHERE c.status = :status", Long.class)
                .setParameter("status", CommitmentStatus.PENDING)
                .getSingleResult();
        res.setPendingCommitments(pendingCommitments != null ? pendingCommitments : 0);

        // New leads this week
        Long newLeads = em.createQuery(
                "SELECT COUNT(l) FROM Lead l WHERE l.createdAt >= :start", Long.class)
                .setParameter("start", startOfWeek)
                .getSingleResult();
        res.setNewLeadsThisWeek(newLeads != null ? newLeads : 0);

        // Leads by stage
        List<Tuple> stages = em.createQuery(
                "SELECT s.name as stageName, COUNT(l) as stageCount FROM Lead l JOIN l.stage s GROUP BY s.name", Tuple.class)
                .getResultList();
        
        Map<String, Long> leadsByStage = new HashMap<>();
        for (Tuple t : stages) {
            leadsByStage.put(t.get(0, String.class), t.get(1, Long.class));
        }
        res.setLeadsByStage(leadsByStage);

        return res;
    }
}
