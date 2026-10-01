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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeadService {
    private final LeadRepository leadRepository;
    private final LeadContactRepository contactRepository;
    private final StageRepository stageRepository;
    private final CategoryRepository categoryRepository;

    public LeadService(LeadRepository leadRepository, LeadContactRepository contactRepository, StageRepository stageRepository, CategoryRepository categoryRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.stageRepository = stageRepository;
        this.categoryRepository = categoryRepository;
    }

    public LeadResponse quickAdd(LeadQuickAddRequest request) {
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            // Check for duplicate phone
            Optional<LeadContact> existingContact = contactRepository.findByValue(request.getPhone());
            if (existingContact.isPresent()) {
                Lead existing = existingContact.get().getLead();
                throw new LeadConflictException(existing.getId(), existing.getFullName());
            }
        }

        Lead lead = new Lead();
        lead.setFullName(request.getName());
        lead.setRemark(request.getNotes());
        lead.setJobTitle(request.getJobTitle());
        lead.setCompanyName(request.getCompany());
        lead.setLinkedinUrl(request.getLinkedinUrl());
        lead.setProfilePictureUrl(request.getProfilePictureUrl());
        
        // Priority mapping
        LeadPriority priority = LeadPriority.WARM;
        if (request.getPriority() != null) {
            String p = request.getPriority().toUpperCase();
            if (p.contains("HIGH") || p.equals("HOT")) priority = LeadPriority.HOT;
            else if (p.contains("LOW") || p.equals("COLD")) priority = LeadPriority.COLD;
        }
        lead.setPriority(priority);

        // Stage mapping
        if (request.getStage() != null) {
            stageRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(request.getStage().trim()))
                .findFirst()
                .ifPresent(lead::setStage);
        }

        // Category mapping
        if (request.getCategoryId() != null) {
            String catIdStr = request.getCategoryId().replace("cat_", "");
            try {
                Long catId = Long.parseLong(catIdStr);
                categoryRepository.findById(catId).ifPresent(c -> lead.setCategories(List.of(c)));
            } catch (Exception ignored) {}
        }

        leadRepository.save(lead);

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            LeadContact contact = new LeadContact();
            contact.setLead(lead);
            contact.setType(ContactType.MOBILE);
            contact.setValue(request.getPhone());
            contact.setIsPrimary(true);
            contactRepository.save(contact);
        }

        return mapToResponse(lead, false);
    }

    public Page<LeadResponse> search(LeadSearchCriteria criteria, Pageable pageable) {
        Specification<Lead> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            
            if (criteria.getQ() != null && !criteria.getQ().trim().isEmpty()) {
                String searchPattern = "%" + criteria.getQ().trim().toLowerCase() + "%";
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("fullName")), searchPattern),
                        cb.like(cb.lower(root.get("firstName")), searchPattern),
                        cb.like(cb.lower(root.get("lastName")), searchPattern)
                    )
                );
            }
            
            if (criteria.getStageId() != null) {
                predicates.add(cb.equal(root.join("stage", JoinType.LEFT).get("id"), criteria.getStageId()));
            }

            if (criteria.getPriority() != null) {
                try {
                    LeadPriority p = LeadPriority.valueOf(criteria.getPriority().toUpperCase());
                    predicates.add(cb.equal(root.get("priority"), p));
                } catch (IllegalArgumentException ignored) {}
            }
            
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
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
        res.setProfilePictureUrl(lead.getProfilePictureUrl());
        
        if (lead.getStage() != null) {
            res.setStageId(lead.getStage().getId());
            res.setStageName(lead.getStage().getName());
        }
        res.setPriority(lead.getPriority());
        res.setNextFollowupAt(lead.getNextFollowupAt());
        
        // Use flat fields if present, fallback to relations for imported data
        res.setJobTitle(lead.getJobTitle());
        res.setCompany(lead.getCompanyName());
        res.setLinkedinUrl(lead.getLinkedinUrl());
        
        if (res.getLinkedinUrl() == null && lead.getLinkedin() != null) {
            res.setLinkedinUrl(lead.getLinkedin().getLinkedinUrl());
        }
        
        if (lead.getJobs() != null && !lead.getJobs().isEmpty()) {
            LeadJob primaryJob = lead.getJobs().stream()
                .filter(j -> Boolean.TRUE.equals(j.getIsCurrent()))
                .findFirst()
                .orElse(lead.getJobs().get(0));
            if (res.getJobTitle() == null) res.setJobTitle(primaryJob.getJobTitle());
            if (res.getCompany() == null && primaryJob.getCompany() != null) {
                res.setCompany(primaryJob.getCompany().getName());
            }
        }
        
        if (lead.getContacts() != null) {
            lead.getContacts().stream()
                .filter(c -> c.getType() == ContactType.MOBILE || c.getType() == ContactType.ASSUMED_MOBILE)
                .findFirst()
                .ifPresent(c -> res.setMobileNumber(c.getValue()));
        }

        return res;
    }
}
