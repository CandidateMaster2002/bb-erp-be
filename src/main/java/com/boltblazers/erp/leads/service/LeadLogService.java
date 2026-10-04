package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.*;
import com.boltblazers.erp.leads.dto.LeadLogRequest;
import com.boltblazers.erp.leads.dto.LeadLogResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeadLogService {

    private final LeadLogRepository logRepository;
    private final LeadRepository leadRepository;

    public LeadLogService(LeadLogRepository logRepository, LeadRepository leadRepository) {
        this.logRepository = logRepository;
        this.leadRepository = leadRepository;
    }

    // ========== Per-Lead Operations ==========

    private Instant parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        dateStr = dateStr.trim();
        if (dateStr.length() == 10) {
            return LocalDate.parse(dateStr).atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
        return Instant.parse(dateStr);
    }

    public LeadLogResponse addLog(Long leadId, LeadLogRequest request) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();

        LeadLog log = new LeadLog();
        log.setLead(lead);
        log.setComment(request.getComment());
        log.setNextAction(request.getNextAction());
        log.setNextActionDate(parseDate(request.getNextActionDate()));
        log.setActionStatus(ActionStatus.PENDING);
        logRepository.save(log);
        return toResponse(log);
    }

    public List<LeadLogResponse> getLogsForLead(Long leadId) {
        return logRepository.findByLeadIdOrderByCreatedAtDesc(leadId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LeadLogResponse updateLog(Long logId, LeadLogRequest request) {
        LeadLog log = logRepository.findById(logId).orElseThrow();
        if (request.getComment() != null) log.setComment(request.getComment());
        if (request.getNextAction() != null) log.setNextAction(request.getNextAction());
        if (request.getNextActionDate() != null) {
            log.setNextActionDate(parseDate(request.getNextActionDate()));
        }
        logRepository.save(log);
        return toResponse(log);
    }

    public void deleteLog(Long logId) {
        logRepository.deleteById(logId);
    }

    // ========== Mark Complete / Cancel ==========

    public LeadLogResponse markCompleted(Long logId) {
        LeadLog log = logRepository.findById(logId).orElseThrow();
        log.setActionStatus(ActionStatus.COMPLETED);
        logRepository.save(log);
        return toResponse(log);
    }

    public LeadLogResponse markCancelled(Long logId) {
        LeadLog log = logRepository.findById(logId).orElseThrow();
        log.setActionStatus(ActionStatus.CANCELLED);
        logRepository.save(log);
        return toResponse(log);
    }

    // ========== Actions View (date-wise, only PENDING) ==========

    public List<LeadLogResponse> getActionsForDate(LocalDate date) {
        Instant start = date.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return logRepository.findPendingActionsForDateRange(start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeadLogResponse> getOverdueAndTodayActions() {
        LocalDate today = LocalDate.now();
        Instant end = today.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return logRepository.findOverdueAndTodayPendingActions(end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeadLogResponse> getActionsBetween(LocalDate from, LocalDate to) {
        Instant start = from.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = to.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return logRepository.findPendingActionsBetween(start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ========== Mapping ==========

    private LeadLogResponse toResponse(LeadLog log) {
        LeadLogResponse res = new LeadLogResponse();
        res.setId(log.getId());
        res.setLeadId(log.getLead().getId());
        res.setLeadName(log.getLead().getFullName());
        res.setComment(log.getComment());
        res.setNextAction(log.getNextAction());
        res.setActionStatus(log.getActionStatus() != null ? log.getActionStatus().name() : null);
        if (log.getNextActionDate() != null) {
            res.setNextActionDate(log.getNextActionDate().toString());
        }
        if (log.getCreatedAt() != null) {
            res.setCreatedAt(log.getCreatedAt().toString());
        }
        return res;
    }
}
