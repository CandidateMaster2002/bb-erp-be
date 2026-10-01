package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Followup;
import com.boltblazers.erp.leads.FollowupRepository;
import com.boltblazers.erp.leads.FollowupStatus;
import com.boltblazers.erp.leads.Lead;
import com.boltblazers.erp.leads.LeadRepository;
import com.boltblazers.erp.leads.dto.FollowupRequest;
import com.boltblazers.erp.leads.dto.FollowupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class FollowupService {

    private final FollowupRepository followupRepository;
    private final LeadRepository leadRepository;

    public FollowupService(FollowupRepository followupRepository, LeadRepository leadRepository) {
        this.followupRepository = followupRepository;
        this.leadRepository = leadRepository;
    }

    public FollowupResponse createFollowup(Long leadId, FollowupRequest request) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
                
        Followup followup = new Followup();
        followup.setLead(lead);
        followup.setDueAt(request.getDueAt());
        followup.setNote(request.getNote());
        followup.setStatus(FollowupStatus.PENDING);
        followup.setRecurrenceDays(request.getRecurrenceDays());
        
        Followup saved = followupRepository.save(followup);
        
        if (lead.getNextFollowupAt() == null || saved.getDueAt().isBefore(lead.getNextFollowupAt())) {
            lead.setNextFollowupAt(saved.getDueAt());
            leadRepository.save(lead);
        }
        
        return mapToResponse(saved);
    }

    public void markDone(Long id) {
        Followup followup = followupRepository.findById(id).orElseThrow();
        followup.setStatus(FollowupStatus.DONE);
        followup.setCompletedAt(Instant.now());
        followupRepository.save(followup);

        if (followup.getRecurrenceDays() != null && followup.getRecurrenceDays() > 0) {
            Followup next = new Followup();
            next.setLead(followup.getLead());
            next.setDueAt(followup.getDueAt().plus(followup.getRecurrenceDays(), ChronoUnit.DAYS));
            next.setNote(followup.getNote());
            next.setRecurrenceDays(followup.getRecurrenceDays());
            next.setStatus(FollowupStatus.PENDING);
            followupRepository.save(next);
        }
    }

    public void acknowledge(Long id) {
        Followup followup = followupRepository.findById(id).orElseThrow();
        followup.setAcknowledgedAt(Instant.now());
        followupRepository.save(followup);
    }
    
    private FollowupResponse mapToResponse(Followup saved) {
        FollowupResponse res = new FollowupResponse();
        res.setId(saved.getId());
        res.setLeadId(saved.getLead().getId());
        res.setDueAt(saved.getDueAt());
        res.setNote(saved.getNote());
        res.setStatus(saved.getStatus());
        res.setRecurrenceDays(saved.getRecurrenceDays());
        res.setCompletedAt(saved.getCompletedAt());
        res.setCreatedAt(saved.getCreatedAt());
        return res;
    }
}
