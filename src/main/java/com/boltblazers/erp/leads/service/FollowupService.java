package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Followup;
import com.boltblazers.erp.leads.FollowupRepository;
import com.boltblazers.erp.leads.FollowupStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class FollowupService {

    private final FollowupRepository followupRepository;

    public FollowupService(FollowupRepository followupRepository) {
        this.followupRepository = followupRepository;
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
}
