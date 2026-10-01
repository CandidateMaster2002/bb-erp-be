package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Interaction;
import com.boltblazers.erp.leads.InteractionRepository;
import com.boltblazers.erp.leads.Lead;
import com.boltblazers.erp.leads.LeadRepository;
import com.boltblazers.erp.leads.dto.InteractionRequest;
import com.boltblazers.erp.leads.dto.InteractionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class InteractionService {

    private final InteractionRepository interactionRepository;
    private final LeadRepository leadRepository;

    public InteractionService(InteractionRepository interactionRepository, LeadRepository leadRepository) {
        this.interactionRepository = interactionRepository;
        this.leadRepository = leadRepository;
    }

    public InteractionResponse logInteraction(Long leadId, InteractionRequest request) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        Interaction interaction = new Interaction();
        interaction.setLead(lead);
        interaction.setType(request.getType());
        interaction.setOutcome(request.getOutcome());
        interaction.setSummary(request.getSummary());
        interaction.setOccurredAt(request.getOccurredAt() != null ? request.getOccurredAt() : Instant.now());
        
        Interaction saved = interactionRepository.save(interaction);
        
        lead.setLastContactedAt(saved.getOccurredAt());
        leadRepository.save(lead);
        
        InteractionResponse res = new InteractionResponse();
        res.setId(saved.getId());
        res.setLeadId(lead.getId());
        res.setType(saved.getType());
        res.setOutcome(saved.getOutcome());
        res.setSummary(saved.getSummary());
        res.setOccurredAt(saved.getOccurredAt());
        res.setCreatedAt(saved.getCreatedAt());
        return res;
    }
}
