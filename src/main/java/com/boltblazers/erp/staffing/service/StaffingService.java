package com.boltblazers.erp.staffing.service;

import com.boltblazers.erp.staffing.dto.StaffingDtos.*;
import com.boltblazers.erp.staffing.entity.*;
import com.boltblazers.erp.staffing.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffingService {

    private final DemandSourceRepository demandSourceRepository;
    private final SupplySourceRepository supplySourceRepository;
    private final RequirementRepository requirementRepository;
    private final CandidateRepository candidateRepository;
    private final SubmissionRepository submissionRepository;

    public StaffingService(DemandSourceRepository demandSourceRepository,
                           SupplySourceRepository supplySourceRepository,
                           RequirementRepository requirementRepository,
                           CandidateRepository candidateRepository,
                           SubmissionRepository submissionRepository) {
        this.demandSourceRepository = demandSourceRepository;
        this.supplySourceRepository = supplySourceRepository;
        this.requirementRepository = requirementRepository;
        this.candidateRepository = candidateRepository;
        this.submissionRepository = submissionRepository;
    }

    // ==================== DEMAND SOURCE (CLIENTS) ====================
    public DemandSourceResponse createDemandSource(DemandSourceRequest req) {
        DemandSource ds = new DemandSource();
        ds.setName(req.getName());
        return mapDS(demandSourceRepository.save(ds));
    }
    public List<DemandSourceResponse> getAllDemandSources() {
        return demandSourceRepository.findAll().stream().map(this::mapDS).collect(Collectors.toList());
    }
    public DemandSourceResponse updateDemandSource(Long id, DemandSourceRequest req) {
        DemandSource ds = demandSourceRepository.findById(id).orElseThrow();
        ds.setName(req.getName());
        return mapDS(demandSourceRepository.save(ds));
    }
    public void deleteDemandSource(Long id) { demandSourceRepository.deleteById(id); }

    // ==================== SUPPLY SOURCE (VENDORS) ====================
    public SupplySourceResponse createSupplySource(SupplySourceRequest req) {
        SupplySource ss = new SupplySource();
        ss.setName(req.getName());
        return mapSS(supplySourceRepository.save(ss));
    }
    public List<SupplySourceResponse> getAllSupplySources() {
        return supplySourceRepository.findAll().stream().map(this::mapSS).collect(Collectors.toList());
    }
    public SupplySourceResponse updateSupplySource(Long id, SupplySourceRequest req) {
        SupplySource ss = supplySourceRepository.findById(id).orElseThrow();
        ss.setName(req.getName());
        return mapSS(supplySourceRepository.save(ss));
    }
    public void deleteSupplySource(Long id) { supplySourceRepository.deleteById(id); }

    // ==================== REQUIREMENTS ====================
    public RequirementResponse createRequirement(RequirementRequest req) {
        Requirement r = new Requirement();
        updateReqFields(r, req);
        return mapReq(requirementRepository.save(r));
    }
    public List<RequirementResponse> getAllRequirements() {
        return requirementRepository.findAll().stream().map(this::mapReq).collect(Collectors.toList());
    }
    public RequirementResponse updateRequirement(Long id, RequirementRequest req) {
        Requirement r = requirementRepository.findById(id).orElseThrow();
        updateReqFields(r, req);
        return mapReq(requirementRepository.save(r));
    }
    public void deleteRequirement(Long id) { requirementRepository.deleteById(id); }

    private void updateReqFields(Requirement r, RequirementRequest req) {
        r.setDemandSource(demandSourceRepository.findById(req.getDemandSourceId()).orElseThrow());
        r.setTitle(req.getTitle());
        r.setJdText(req.getJdText());
        r.setCtc(req.getCtc());
        r.setNoticePeriod(req.getNoticePeriod());
        r.setExperienceRange(req.getExperienceRange());
        r.setDescription(req.getDescription());
    }

    // ==================== CANDIDATES ====================
    public CandidateResponse createCandidate(CandidateRequest req) {
        Candidate c = new Candidate();
        updateCandFields(c, req);
        return mapCand(candidateRepository.save(c));
    }
    public List<CandidateResponse> getAllCandidates() {
        return candidateRepository.findAll().stream().map(this::mapCand).collect(Collectors.toList());
    }
    public CandidateResponse updateCandidate(Long id, CandidateRequest req) {
        Candidate c = candidateRepository.findById(id).orElseThrow();
        updateCandFields(c, req);
        return mapCand(candidateRepository.save(c));
    }
    public void deleteCandidate(Long id) { candidateRepository.deleteById(id); }

    private void updateCandFields(Candidate c, CandidateRequest req) {
        if (req.getSupplySourceId() != null) {
            c.setSupplySource(supplySourceRepository.findById(req.getSupplySourceId()).orElse(null));
        }
        c.setName(req.getName());
        c.setMobileNo(req.getMobileNo());
        c.setResumeLink(req.getResumeLink());
        c.setCurrentCtc(req.getCurrentCtc());
        c.setExpectedCtc(req.getExpectedCtc());
        c.setNoticePeriod(req.getNoticePeriod());
        c.setGap(req.getGap());
        c.setCollegeTier(req.getCollegeTier());
    }

    // ==================== SUBMISSIONS ====================
    public SubmissionResponse createSubmission(SubmissionRequest req) {
        Submission s = new Submission();
        s.setRequirement(requirementRepository.findById(req.getRequirementId()).orElseThrow());
        s.setCandidate(candidateRepository.findById(req.getCandidateId()).orElseThrow());
        if (req.getStatus() != null) s.setStatus(SubmissionStatus.valueOf(req.getStatus().toUpperCase()));
        s.setNotes(req.getNotes());
        return mapSub(submissionRepository.save(s));
    }
    public List<SubmissionResponse> getAllSubmissions() {
        return submissionRepository.findAll().stream().map(this::mapSub).collect(Collectors.toList());
    }
    public SubmissionResponse updateSubmission(Long id, SubmissionRequest req) {
        Submission s = submissionRepository.findById(id).orElseThrow();
        if (req.getStatus() != null) s.setStatus(SubmissionStatus.valueOf(req.getStatus().toUpperCase()));
        s.setNotes(req.getNotes());
        return mapSub(submissionRepository.save(s));
    }
    public void deleteSubmission(Long id) { submissionRepository.deleteById(id); }


    // ==================== MAPPERS ====================
    private DemandSourceResponse mapDS(DemandSource ds) {
        if (ds == null) return null;
        DemandSourceResponse r = new DemandSourceResponse();
        r.setId(ds.getId());
        r.setName(ds.getName());
        r.setCreatedAt(ds.getCreatedAt() != null ? ds.getCreatedAt().toString() : null);
        return r;
    }
    private SupplySourceResponse mapSS(SupplySource ss) {
        if (ss == null) return null;
        SupplySourceResponse r = new SupplySourceResponse();
        r.setId(ss.getId());
        r.setName(ss.getName());
        r.setCreatedAt(ss.getCreatedAt() != null ? ss.getCreatedAt().toString() : null);
        return r;
    }
    private RequirementResponse mapReq(Requirement req) {
        if (req == null) return null;
        RequirementResponse r = new RequirementResponse();
        r.setId(req.getId());
        r.setDemandSource(mapDS(req.getDemandSource()));
        r.setTitle(req.getTitle());
        r.setJdText(req.getJdText());
        r.setCtc(req.getCtc());
        r.setNoticePeriod(req.getNoticePeriod());
        r.setExperienceRange(req.getExperienceRange());
        r.setDescription(req.getDescription());
        r.setCreatedAt(req.getCreatedAt() != null ? req.getCreatedAt().toString() : null);
        return r;
    }
    private CandidateResponse mapCand(Candidate c) {
        if (c == null) return null;
        CandidateResponse r = new CandidateResponse();
        r.setId(c.getId());
        r.setSupplySource(mapSS(c.getSupplySource()));
        r.setName(c.getName());
        r.setMobileNo(c.getMobileNo());
        r.setResumeLink(c.getResumeLink());
        r.setCurrentCtc(c.getCurrentCtc());
        r.setExpectedCtc(c.getExpectedCtc());
        r.setNoticePeriod(c.getNoticePeriod());
        r.setGap(c.getGap());
        r.setCollegeTier(c.getCollegeTier());
        r.setCreatedAt(c.getCreatedAt() != null ? c.getCreatedAt().toString() : null);
        return r;
    }
    private SubmissionResponse mapSub(Submission s) {
        if (s == null) return null;
        SubmissionResponse r = new SubmissionResponse();
        r.setId(s.getId());
        r.setRequirement(mapReq(s.getRequirement()));
        r.setCandidate(mapCand(s.getCandidate()));
        r.setStatus(s.getStatus().name());
        r.setNotes(s.getNotes());
        r.setCreatedAt(s.getCreatedAt() != null ? s.getCreatedAt().toString() : null);
        return r;
    }
}
