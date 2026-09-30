package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.*;
import com.boltblazers.erp.leads.dto.ImportSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BatchImportProcessor {

    private final LeadRepository leadRepository;
    private final LeadContactRepository contactRepository;
    private final LeadLinkedinRepository linkedinRepository;
    private final CompanyRepository companyRepository;
    private final LeadJobRepository jobRepository;
    private final LeadEducationRepository educationRepository;
    private final StageRepository stageRepository;
    private final CategoryRepository categoryRepository;

    public BatchImportProcessor(
            LeadRepository leadRepository,
            LeadContactRepository contactRepository,
            LeadLinkedinRepository linkedinRepository,
            CompanyRepository companyRepository,
            LeadJobRepository jobRepository,
            LeadEducationRepository educationRepository,
            StageRepository stageRepository,
            CategoryRepository categoryRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.linkedinRepository = linkedinRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.educationRepository = educationRepository;
        this.stageRepository = stageRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processBatch(
            List<Map<String, String>> batch, 
            Import imp, 
            List<Long> defaultCategoryIds, 
            Long defaultStageId, 
            ImportSummaryResponse summary) {

        // Fetch defaults
        Stage defaultStage = null;
        if (defaultStageId != null) {
            defaultStage = stageRepository.findById(defaultStageId).orElse(null);
        }
        if (defaultStage == null) {
            defaultStage = stageRepository.findAll().stream()
                    .filter(s -> "New".equalsIgnoreCase(s.getName())).findFirst().orElse(null);
        }

        List<Category> defaultCategories = new ArrayList<>();
        if (defaultCategoryIds != null) {
            defaultCategories = categoryRepository.findAllById(defaultCategoryIds);
        }

        for (Map<String, String> row : batch) {
            try {
                processRow(row, imp, defaultStage, defaultCategories, summary);
            } catch (Exception e) {
                int rowIndex = Integer.parseInt(row.getOrDefault("_row_index", "0"));
                summary.getErrors().add(new ImportSummaryResponse.RowError(rowIndex, e.getMessage()));
                summary.setSkipped(summary.getSkipped() + 1);
            }
        }
    }

    private void processRow(Map<String, String> row, Import imp, Stage defaultStage, List<Category> defaultCategories, ImportSummaryResponse summary) {
        String linkedinUrl = row.get("linkedin_url");
        String linkedinPublicId = row.get("public_identifier");
        String mobile = row.get("mobile_number");
        String personalEmail = row.get("personal_email");
        String workEmail = row.get("work_email");

        Lead existingLead = findExistingLead(linkedinUrl, linkedinPublicId, mobile, personalEmail, workEmail);
        
        boolean isNew = (existingLead == null);
        Lead lead = isNew ? new Lead() : existingLead;

        // Map Lead fields (only update if empty)
        updateIfEmpty(lead, "fullName", row.get("full_name"));
        updateIfEmpty(lead, "firstName", row.get("first_name"));
        updateIfEmpty(lead, "lastName", row.get("last_name"));
        updateIfEmpty(lead, "headline", row.get("headline"));
        updateIfEmpty(lead, "summary", row.get("summary"));
        updateIfEmpty(lead, "city", row.get("city"));
        updateIfEmpty(lead, "state", row.get("state"));
        updateIfEmpty(lead, "country", row.get("country"));
        updateIfEmpty(lead, "location", row.get("location"));
        updateIfEmpty(lead, "profilePictureUrl", row.get("profile_picture_url"));
        updateIfEmpty(lead, "remark", row.get("remark"));

        if (isNew) {
            lead.setSourceImport(imp);
            lead.setPriority(LeadPriority.WARM);
            lead.setSource("IMPORT");
            
            // Match stage
            String status = row.get("lead_status");
            Stage stage = defaultStage;
            if (StringUtils.hasText(status)) {
                stage = stageRepository.findAll().stream()
                        .filter(s -> s.getName().equalsIgnoreCase(status.trim()))
                        .findFirst().orElse(defaultStage);
            }
            lead.setStage(stage);
            
            // Categories
            Set<Category> categories = new HashSet<>(defaultCategories);
            addCategory(categories, row.get("category_1"));
            addCategory(categories, row.get("category_2"));
            lead.setCategories(new ArrayList<>(categories));
        }

        leadRepository.save(lead);

        // Contacts
        addContact(lead, ContactType.MOBILE, mobile, isNew);
        addContact(lead, ContactType.ASSUMED_MOBILE, row.get("assumed_mobile_no"), false);
        addContact(lead, ContactType.PERSONAL_EMAIL, personalEmail, false);
        addContact(lead, ContactType.WORK_EMAIL, workEmail, false);

        // LinkedIn
        if (StringUtils.hasText(linkedinUrl) || StringUtils.hasText(linkedinPublicId)) {
            LeadLinkedin ll = linkedinRepository.findById(lead.getId()).orElse(new LeadLinkedin());
            ll.setLead(lead);
            if (StringUtils.hasText(linkedinUrl)) ll.setLinkedinUrl(linkedinUrl);
            if (StringUtils.hasText(linkedinPublicId)) ll.setPublicIdentifier(linkedinPublicId);
            if (StringUtils.hasText(row.get("member_urn"))) ll.setMemberUrn(row.get("member_urn"));
            if (StringUtils.hasText(row.get("personal_website"))) ll.setPersonalWebsite(row.get("personal_website"));
            if (StringUtils.hasText(row.get("followers_count"))) {
                try { ll.setFollowersCount(Integer.parseInt(row.get("followers_count"))); } catch(Exception ignored){}
            }
            if (StringUtils.hasText(row.get("premium"))) {
                ll.setPremium(Boolean.parseBoolean(row.get("premium")));
            }
            linkedinRepository.save(ll);
        }

        // Company and Job
        String companyName = row.get("company_name");
        String companyLinkedinId = row.get("company_linkedin_id");
        if (StringUtils.hasText(companyName) || StringUtils.hasText(companyLinkedinId)) {
            Company company = null;
            if (StringUtils.hasText(companyLinkedinId)) {
                company = companyRepository.findAll().stream()
                        .filter(c -> companyLinkedinId.equals(c.getLinkedinId())).findFirst().orElse(null);
            }
            if (company == null && StringUtils.hasText(companyName)) {
                company = companyRepository.findAll().stream()
                        .filter(c -> companyName.equalsIgnoreCase(c.getName())).findFirst().orElse(null);
            }
            if (company == null) {
                company = new Company();
                company.setName(StringUtils.hasText(companyName) ? companyName : "Unknown");
                company.setLinkedinId(companyLinkedinId);
                company.setLinkedinUrl(row.get("company_linkedin_url"));
                company.setDescription(row.get("company_description"));
                company.setEmployees(row.get("company_employees"));
                company.setIndustry(row.get("company_industry"));
                company.setLocation(row.get("company_location"));
                company.setSpecialities(row.get("company_specialities"));
                company.setWebsite(row.get("company_website"));
                if (StringUtils.hasText(row.get("company_founded_year"))) {
                    try { company.setFoundedYear(Integer.parseInt(row.get("company_founded_year"))); } catch(Exception ignored){}
                }
                companyRepository.save(company);
            }

            // Job
            String jobTitle = row.get("job_title");
            if (StringUtils.hasText(jobTitle)) {
                LeadJob job = new LeadJob();
                job.setLead(lead);
                job.setCompany(company);
                job.setJobTitle(jobTitle);
                job.setJobDescription(row.get("job_description"));
                job.setStartedOn(row.get("job_started_on"));
                job.setIsCurrent(true);
                if (StringUtils.hasText(row.get("recently_hired"))) {
                    job.setRecentlyHired(Boolean.parseBoolean(row.get("recently_hired")));
                }
                jobRepository.save(job);
            }
        }

        // Education
        String college = row.get("colleges");
        if (StringUtils.hasText(college)) {
            LeadEducation edu = new LeadEducation();
            edu.setLead(lead);
            edu.setCollege(college);
            edu.setBranch(row.get("branch"));
            edu.setDegree(row.get("degree"));
            edu.setBatchStart(row.get("ism_batch_start"));
            edu.setBatchEnd(row.get("ism_batch_end"));
            educationRepository.save(edu);
        }

        if (isNew) {
            summary.setCreated(summary.getCreated() + 1);
        } else {
            summary.setUpdated(summary.getUpdated() + 1);
        }
    }

    private void addCategory(Set<Category> categories, String catName) {
        if (!StringUtils.hasText(catName)) return;
        Category cat = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(catName.trim()))
                .findFirst().orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(catName.trim());
                    return categoryRepository.save(newCat);
                });
        categories.add(cat);
    }

    private void addContact(Lead lead, ContactType type, String value, boolean isPrimaryIfFirst) {
        if (!StringUtils.hasText(value)) return;
        boolean exists = contactRepository.findByValue(value).isPresent();
        if (!exists) {
            LeadContact contact = new LeadContact();
            contact.setLead(lead);
            contact.setType(type);
            contact.setValue(value.trim());
            contact.setIsPrimary(isPrimaryIfFirst);
            contactRepository.save(contact);
        }
    }

    private Lead findExistingLead(String linkedinUrl, String linkedinPublicId, String mobile, String personalEmail, String workEmail) {
        if (StringUtils.hasText(linkedinPublicId)) {
            LeadLinkedin ll = linkedinRepository.findAll().stream()
                    .filter(l -> linkedinPublicId.equals(l.getPublicIdentifier())).findFirst().orElse(null);
            if (ll != null) return ll.getLead();
        }
        if (StringUtils.hasText(linkedinUrl)) {
            LeadLinkedin ll = linkedinRepository.findAll().stream()
                    .filter(l -> linkedinUrl.equals(l.getLinkedinUrl())).findFirst().orElse(null);
            if (ll != null) return ll.getLead();
        }
        return findByContact(mobile, personalEmail, workEmail);
    }

    private Lead findByContact(String... values) {
        for (String val : values) {
            if (StringUtils.hasText(val)) {
                LeadContact contact = contactRepository.findByValue(val.trim()).orElse(null);
                if (contact != null) return contact.getLead();
            }
        }
        return null;
    }

    private void updateIfEmpty(Lead lead, String field, String value) {
        if (!StringUtils.hasText(value)) return;
        try {
            java.lang.reflect.Field f = Lead.class.getDeclaredField(field);
            f.setAccessible(true);
            Object current = f.get(lead);
            if (current == null || (current instanceof String && !StringUtils.hasText((String)current))) {
                f.set(lead, value);
            }
        } catch (Exception ignored) { }
    }
}
