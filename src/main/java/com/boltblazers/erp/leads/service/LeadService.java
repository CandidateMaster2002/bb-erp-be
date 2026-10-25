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
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final LeadEducationRepository educationRepository;

    public LeadService(LeadRepository leadRepository, LeadContactRepository contactRepository, CategoryRepository categoryRepository, LeadEducationRepository educationRepository, TagRepository tagRepository, TagService tagService) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.categoryRepository = categoryRepository;
        this.educationRepository = educationRepository;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
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
        
        if (request.getPersonalEmail() != null && !request.getPersonalEmail().trim().isEmpty()) {
            // Check for duplicate email
            Optional<LeadContact> existingContact = contactRepository.findByValue(request.getPersonalEmail());
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
        
        if (request.getRecordType() != null) {
            lead.setRecordType(request.getRecordType());
        }
        
        // Detailed fields
        lead.setCity(request.getCity());
        lead.setState(request.getState());
        lead.setCountry(request.getCountry());
        lead.setLocation(request.getLocation());
        lead.setHeadline(request.getHeadline());
        lead.setSummary(request.getSummary());
        
        // Priority mapping
        LeadPriority priority = LeadPriority.WARM;
        if (request.getPriority() != null) {
            String p = request.getPriority().toUpperCase();
            if (p.contains("HIGH") || p.equals("HOT")) priority = LeadPriority.HOT;
            else if (p.contains("LOW") || p.equals("COLD")) priority = LeadPriority.COLD;
        }
        lead.setPriority(priority);

        // Category mapping
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            lead.setCategories(categories);
        }

        if ((request.getTagIds() != null && !request.getTagIds().isEmpty()) ||
            (request.getTagNames() != null && !request.getTagNames().isEmpty())) {
            List<Tag> tags = tagService.resolveTags(request.getTagIds(), request.getTagNames());
            lead.setTags(tags);
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

        if (request.getPersonalEmail() != null && !request.getPersonalEmail().trim().isEmpty()) {
            LeadContact contact = new LeadContact();
            contact.setLead(lead);
            contact.setType(ContactType.PERSONAL_EMAIL);
            contact.setValue(request.getPersonalEmail());
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
                
                jakarta.persistence.criteria.Subquery<Long> contactSubquery = query.subquery(Long.class);
                jakarta.persistence.criteria.Root<LeadContact> contactRoot = contactSubquery.from(LeadContact.class);
                contactSubquery.select(contactRoot.get("lead").get("id"));
                contactSubquery.where(cb.like(cb.lower(contactRoot.get("value")), searchPattern));

                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("fullName")), searchPattern),
                        cb.like(cb.lower(root.get("firstName")), searchPattern),
                        cb.like(cb.lower(root.get("lastName")), searchPattern),
                        cb.like(cb.lower(root.get("companyName")), searchPattern),
                        cb.like(cb.lower(root.get("jobTitle")), searchPattern),
                        cb.like(cb.lower(root.get("city")), searchPattern),
                        cb.like(cb.lower(root.get("state")), searchPattern),
                        cb.like(cb.lower(root.get("country")), searchPattern),
                        cb.like(cb.lower(root.get("location")), searchPattern),
                        cb.like(cb.lower(root.get("headline")), searchPattern),
                        cb.like(cb.lower(root.get("summary")), searchPattern),
                        cb.like(cb.lower(root.get("linkedinUrl")), searchPattern),
                        cb.like(cb.lower(root.get("remark")), searchPattern),
                        root.get("id").in(contactSubquery)
                    )
                );
            }
            
            if (criteria.getCategoryId() != null) {
                predicates.add(cb.equal(root.join("categories", JoinType.LEFT).get("id"), criteria.getCategoryId()));
            }

            query.distinct(true);

            if (criteria.getTagId() != null) {
                predicates.add(cb.equal(root.join("tags", JoinType.LEFT).get("id"), criteria.getTagId()));
            }

            if (criteria.getTagIds() != null && !criteria.getTagIds().isEmpty()) {
                predicates.add(root.join("tags", JoinType.LEFT).get("id").in(criteria.getTagIds()));
            }

            if (criteria.getRecordType() != null) {
                predicates.add(cb.equal(root.get("recordType"), criteria.getRecordType()));
            } else {
                predicates.add(cb.equal(root.get("recordType"), "LEAD"));
            }

            if (criteria.getExcludeCategoryId() != null) {
                jakarta.persistence.criteria.Subquery<Long> excludeSub = query.subquery(Long.class);
                jakarta.persistence.criteria.Root<Lead> subRoot = excludeSub.from(Lead.class);
                excludeSub.select(subRoot.get("id"));
                excludeSub.where(cb.equal(subRoot.join("categories").get("id"), criteria.getExcludeCategoryId()));
                predicates.add(cb.not(root.get("id").in(excludeSub)));
            }
            
            if (Boolean.TRUE.equals(criteria.getHasMobileNo())) {
                jakarta.persistence.criteria.Subquery<Long> sub = query.subquery(Long.class);
                jakarta.persistence.criteria.Root<LeadContact> subRoot = sub.from(LeadContact.class);
                sub.select(subRoot.get("lead").get("id"));
                sub.where(
                    cb.and(
                        cb.or(
                            cb.equal(subRoot.get("type"), ContactType.MOBILE),
                            cb.equal(subRoot.get("type"), ContactType.ASSUMED_MOBILE),
                            cb.equal(subRoot.get("type"), ContactType.WHATSAPP)
                        ),
                        cb.isNotNull(subRoot.get("value")),
                        cb.notEqual(cb.trim(subRoot.get("value")), ""),
                        cb.notEqual(cb.trim(subRoot.get("value")), "-"),
                        cb.notEqual(cb.lower(cb.trim(subRoot.get("value"))), "null"),
                        cb.notEqual(cb.lower(cb.trim(subRoot.get("value"))), "n/a")
                    )
                );
                predicates.add(root.get("id").in(sub));
            } else if (Boolean.FALSE.equals(criteria.getHasMobileNo())) {
                jakarta.persistence.criteria.Subquery<Long> sub = query.subquery(Long.class);
                jakarta.persistence.criteria.Root<LeadContact> subRoot = sub.from(LeadContact.class);
                sub.select(subRoot.get("lead").get("id"));
                sub.where(
                    cb.and(
                        cb.or(
                            cb.equal(subRoot.get("type"), ContactType.MOBILE),
                            cb.equal(subRoot.get("type"), ContactType.ASSUMED_MOBILE),
                            cb.equal(subRoot.get("type"), ContactType.WHATSAPP)
                        ),
                        cb.isNotNull(subRoot.get("value")),
                        cb.notEqual(cb.trim(subRoot.get("value")), ""),
                        cb.notEqual(cb.trim(subRoot.get("value")), "-"),
                        cb.notEqual(cb.lower(cb.trim(subRoot.get("value"))), "null"),
                        cb.notEqual(cb.lower(cb.trim(subRoot.get("value"))), "n/a")
                    )
                );
                predicates.add(cb.not(root.get("id").in(sub)));
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

    public LeadResponse updateCategories(Long leadId, List<Long> categoryIds) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        lead.setCategories(categories);
        leadRepository.save(lead);
        return mapToResponse(lead, false);
    }

    public LeadResponse updateTags(Long leadId, List<Long> tagIds) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        List<Tag> tags = (tagIds != null && !tagIds.isEmpty()) ? tagRepository.findAllById(tagIds) : new ArrayList<>();
        lead.setTags(tags);
        leadRepository.save(lead);
        return mapToResponse(lead, false);
    }

    public LeadResponse addTagNames(Long leadId, List<String> tagNames) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        List<Tag> currentTags = new ArrayList<>(lead.getTags());
        if (tagNames != null) {
            for (String name : tagNames) {
                if (name != null && !name.trim().isEmpty()) {
                    Tag tag = tagService.getOrCreateTag(name);
                    if (!currentTags.contains(tag)) {
                        currentTags.add(tag);
                    }
                }
            }
        }
        lead.setTags(currentTags);
        leadRepository.save(lead);
        return mapToResponse(lead, false);
    }

    public LeadResponse removeTag(Long leadId, Long tagId) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        lead.getTags().removeIf(t -> t.getId().equals(tagId));
        leadRepository.save(lead);
        return mapToResponse(lead, false);
    }

    public LeadResponse updateLead(Long leadId, com.boltblazers.erp.leads.dto.LeadUpdateRequest request) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        
        if (request.getFullName() != null) lead.setFullName(request.getFullName());
        if (request.getHeadline() != null) lead.setHeadline(request.getHeadline());
        if (request.getSummary() != null) lead.setSummary(request.getSummary());
        if (request.getCity() != null) lead.setCity(request.getCity());
        if (request.getState() != null) lead.setState(request.getState());
        if (request.getCountry() != null) lead.setCountry(request.getCountry());
        if (request.getLocation() != null) lead.setLocation(request.getLocation());
        if (request.getProfilePictureUrl() != null) lead.setProfilePictureUrl(request.getProfilePictureUrl());
        
        if (request.getJobTitle() != null) lead.setJobTitle(request.getJobTitle());
        if (request.getCompany() != null) lead.setCompanyName(request.getCompany());
        if (request.getLinkedinUrl() != null) lead.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getRemark() != null) lead.setRemark(request.getRemark());
        if (request.getPriority() != null) lead.setPriority(request.getPriority());
        if (request.getTagIds() != null || request.getTagNames() != null) {
            lead.setTags(tagService.resolveTags(request.getTagIds(), request.getTagNames()));
        }

        leadRepository.save(lead);
        
        if (request.getMobileNumber() != null) {
            List<LeadContact> contacts = contactRepository.findByLead(lead);
            LeadContact mobile = contacts.stream()
                .filter(c -> c.getType() == ContactType.MOBILE || c.getType() == ContactType.ASSUMED_MOBILE)
                .findFirst()
                .orElseGet(() -> {
                    LeadContact c = new LeadContact();
                    c.setLead(lead);
                    c.setType(ContactType.MOBILE);
                    c.setIsPrimary(true);
                    return c;
                });
            mobile.setValue(request.getMobileNumber());
            contactRepository.save(mobile);
        }

        if (request.getPersonalEmail() != null) {
            List<LeadContact> contacts = contactRepository.findByLead(lead);
            LeadContact email = contacts.stream()
                .filter(c -> c.getType() == ContactType.PERSONAL_EMAIL)
                .findFirst()
                .orElseGet(() -> {
                    LeadContact c = new LeadContact();
                    c.setLead(lead);
                    c.setType(ContactType.PERSONAL_EMAIL);
                    c.setIsPrimary(true);
                    return c;
                });
            email.setValue(request.getPersonalEmail());
            contactRepository.save(email);
        }

        return mapToResponse(lead, true);
    }

    private LeadResponse mapToResponse(Lead lead, boolean includeRelations) {
        LeadResponse res = new LeadResponse();
        res.setId(lead.getId());
        res.setFullName(lead.getFullName());
        res.setFirstName(lead.getFirstName());
        res.setLastName(lead.getLastName());
        res.setHeadline(lead.getHeadline());
        res.setSummary(lead.getSummary());
        res.setRecordType(lead.getRecordType());
        res.setCity(lead.getCity());
        res.setState(lead.getState());
        res.setCountry(lead.getCountry());
        res.setLocation(lead.getLocation());
        res.setProfilePictureUrl(lead.getProfilePictureUrl());
        res.setPriority(lead.getPriority());
        res.setSource(lead.getSource());
        res.setRemark(lead.getRemark());
        res.setNextFollowupAt(lead.getNextFollowupAt());
        res.setLastContactedAt(lead.getLastContactedAt());
        res.setLostReason(lead.getLostReason());
        res.setCreatedAt(lead.getCreatedAt());
        res.setUpdatedAt(lead.getUpdatedAt());
        
        // Use flat fields
        res.setJobTitle(lead.getJobTitle());
        res.setCompany(lead.getCompanyName());
        res.setLinkedinUrl(lead.getLinkedinUrl());
        
        // Contacts
        List<LeadContact> contacts = contactRepository.findByLead(lead);
        if (contacts != null) {
            contacts.stream()
                .filter(c -> (c.getType() == ContactType.MOBILE || c.getType() == ContactType.ASSUMED_MOBILE || c.getType() == ContactType.WHATSAPP)
                             && c.getValue() != null 
                             && !c.getValue().trim().isEmpty() 
                             && !"-".equals(c.getValue().trim()) 
                             && !"null".equalsIgnoreCase(c.getValue().trim())
                             && !"n/a".equalsIgnoreCase(c.getValue().trim()))
                .findFirst()
                .ifPresent(c -> res.setMobileNumber(c.getValue()));

            if (includeRelations) {
                res.setContacts(contacts.stream().map(c -> {
                    LeadResponse.ContactDto dto = new LeadResponse.ContactDto();
                    dto.setId(c.getId());
                    dto.setType(c.getType().name());
                    dto.setValue(c.getValue());
                    dto.setIsPrimary(c.getIsPrimary());
                    return dto;
                }).collect(java.util.stream.Collectors.toList()));
            }
        }

        // Map categories
        if (lead.getCategories() != null && !lead.getCategories().isEmpty()) {
            res.setCategories(lead.getCategories().stream().map(cat -> {
                LeadResponse.CategoryDto dto = new LeadResponse.CategoryDto();
                dto.setId(cat.getId());
                dto.setName(cat.getName());
                return dto;
            }).collect(java.util.stream.Collectors.toList()));
        }

        // Map tags
        if (lead.getTags() != null && !lead.getTags().isEmpty()) {
            res.setTags(lead.getTags().stream().map(tag -> {
                LeadResponse.TagDto dto = new LeadResponse.TagDto();
                dto.setId(tag.getId());
                dto.setName(tag.getName());
                return dto;
            }).collect(java.util.stream.Collectors.toList()));
        }

        if (includeRelations) {
            List<LeadEducation> eduList = educationRepository.findByLead(lead);
            if (eduList != null && !eduList.isEmpty()) {
                res.setEducation(eduList.stream().map(e -> {
                    LeadResponse.EducationDto dto = new LeadResponse.EducationDto();
                    dto.setId(e.getId());
                    dto.setCollege(e.getCollege());
                    dto.setDegree(e.getDegree());
                    dto.setBranch(e.getBranch());
                    dto.setBatchStart(e.getBatchStart());
                    dto.setBatchEnd(e.getBatchEnd());
                    return dto;
                }).collect(java.util.stream.Collectors.toList()));
            }
        }

        return res;
    }
}

