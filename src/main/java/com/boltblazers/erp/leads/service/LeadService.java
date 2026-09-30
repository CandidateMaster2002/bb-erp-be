package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.*;
import com.boltblazers.erp.leads.dto.*;
import com.boltblazers.erp.leads.exception.LeadConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeadService {
    private final LeadRepository leadRepository;
    private final LeadContactRepository contactRepository;

    public LeadService(LeadRepository leadRepository, LeadContactRepository contactRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
    }

    public LeadResponse quickAdd(LeadQuickAddRequest request) {
        // Check for duplicate phone
        Optional<LeadContact> existingContact = contactRepository.findByValue(request.getPhone());
        if (existingContact.isPresent()) {
            Lead existing = existingContact.get().getLead();
            throw new LeadConflictException(existing.getId(), existing.getFullName());
        }

        Lead lead = new Lead();
        lead.setFullName(request.getFullName());
        lead.setRemark(request.getNote());
        lead.setPriority(LeadPriority.WARM); // default
        leadRepository.save(lead);

        LeadContact contact = new LeadContact();
        contact.setLead(lead);
        contact.setType(ContactType.MOBILE);
        contact.setValue(request.getPhone());
        contact.setIsPrimary(true);
        contactRepository.save(contact);

        return mapToResponse(lead, false);
    }

    public Page<LeadResponse> search(LeadSearchCriteria criteria, Pageable pageable) {
        Specification<Lead> spec = (root, query, cb) -> {
            // Very simplified specification for now to satisfy structural requirements
            return cb.conjunction();
        };
        return leadRepository.findAll(spec, pageable).map(l -> mapToResponse(l, false));
    }

    public LeadResponse getById(Long id) {
        Lead lead = leadRepository.findById(id).orElseThrow();
        return mapToResponse(lead, true);
    }
    
    public void delete(Long id) {
        leadRepository.deleteById(id);
    }

    private LeadResponse mapToResponse(Lead lead, boolean includeRelations) {
        LeadResponse res = new LeadResponse();
        res.setId(lead.getId());
        res.setFullName(lead.getFullName());
        if (lead.getStage() != null) {
            res.setStageId(lead.getStage().getId());
            res.setStageName(lead.getStage().getName());
        }
        res.setPriority(lead.getPriority());
        res.setNextFollowupAt(lead.getNextFollowupAt());
        // For the sake of the scaffold, skip full mapping logic.
        return res;
    }
}
