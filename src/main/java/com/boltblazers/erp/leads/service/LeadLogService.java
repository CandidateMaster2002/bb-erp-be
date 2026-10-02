package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Lead;
import com.boltblazers.erp.leads.LeadLog;
import com.boltblazers.erp.leads.LeadLogRepository;
import com.boltblazers.erp.leads.LeadRepository;
import com.boltblazers.erp.leads.dto.LeadLogRequest;
import com.boltblazers.erp.leads.dto.LeadLogResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public LeadLogResponse addLog(Long leadId, LeadLogRequest request) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();

        LeadLog log = new LeadLog();
        log.setLead(lead);
        log.setComment(request.getComment());
        log.setNextAction(request.getNextAction());
        if (request.getNextActionDate() != null && !request.getNextActionDate().isBlank()) {
            log.setNextActionDate(LocalDate.parse(request.getNextActionDate()));
        }
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
        if (request.getNextActionDate() != null && !request.getNextActionDate().isBlank()) {
            log.setNextActionDate(LocalDate.parse(request.getNextActionDate()));
        }
        logRepository.save(log);
        return toResponse(log);
    }

    public void deleteLog(Long logId) {
        logRepository.deleteById(logId);
    }

    // ========== Actions View (date-wise) ==========

    public List<LeadLogResponse> getActionsForDate(LocalDate date) {
        return logRepository.findByNextActionDateOrderByNextActionDateAsc(date)
                .stream()
                .filter(l -> l.getNextAction() != null && !l.getNextAction().isBlank())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LeadLogResponse> getOverdueAndTodayActions() {
        LocalDate today = LocalDate.now();
        return logRepository.findByNextActionDateLessThanEqualAndNextActionIsNotNullOrderByNextActionDateAsc(today)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeadLogResponse> getActionsBetween(LocalDate from, LocalDate to) {
        return logRepository.findByNextActionDateBetweenOrderByNextActionDateAsc(from, to)
                .stream()
                .filter(l -> l.getNextAction() != null && !l.getNextAction().isBlank())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ========== Mapping ==========

    private LeadLogResponse toResponse(LeadLog log) {
        LeadLogResponse res = new LeadLogResponse();
        res.setId(log.getId());
        res.setLeadId(log.getLead().getId());
        res.setLeadName(log.getLead().getFullName());
        res.setComment(log.getComment());
        res.setNextAction(log.getNextAction());
        if (log.getNextActionDate() != null) {
            res.setNextActionDate(log.getNextActionDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (log.getCreatedAt() != null) {
            res.setCreatedAt(log.getCreatedAt().toString());
        }
        return res;
    }
}
