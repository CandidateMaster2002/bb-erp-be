package com.boltblazers.erp.staffing.dto;

import lombok.Data;
import java.util.List;

public class StaffingDtos {

    // ========== DEMAND SOURCE (CLIENT) ==========
    @Data
    public static class DemandSourceRequest {
        private String name;
    }
    
    @Data
    public static class DemandSourceResponse {
        private Long id;
        private String name;
        private String createdAt;
    }

    // ========== REQUIREMENT (JOB ORDER) ==========
    @Data
    public static class RequirementRequest {
        private Long demandSourceId;
        private String title;
        private String jdText;
        private String ctc;
        private String noticePeriod;
        private String experienceRange;
        private String description;
    }

    @Data
    public static class RequirementResponse {
        private Long id;
        private DemandSourceResponse demandSource;
        private String title;
        private String jdText;
        private String ctc;
        private String noticePeriod;
        private String experienceRange;
        private String description;
        private String createdAt;
    }

    // ========== SUPPLY SOURCE (VENDOR) ==========
    @Data
    public static class SupplySourceRequest {
        private String name;
    }

    @Data
    public static class SupplySourceResponse {
        private Long id;
        private String name;
        private String createdAt;
    }

    // ========== CANDIDATE ==========
    @Data
    public static class CandidateRequest {
        private Long supplySourceId;
        private String name;
        private String mobileNo;
        private String resumeLink;
        private String currentCtc;
        private String expectedCtc;
        private String noticePeriod;
        private String gap;
        private String collegeTier;
    }

    @Data
    public static class CandidateResponse {
        private Long id;
        private SupplySourceResponse supplySource;
        private String name;
        private String mobileNo;
        private String resumeLink;
        private String currentCtc;
        private String expectedCtc;
        private String noticePeriod;
        private String gap;
        private String collegeTier;
        private String createdAt;
    }

    // ========== SUBMISSION ==========
    @Data
    public static class SubmissionRequest {
        private Long requirementId;
        private Long candidateId;
        private String status; // APPLIED, SCREENING, INTERVIEW, OFFERED, HIRED, REJECTED
        private String notes;
    }

    @Data
    public static class SubmissionResponse {
        private Long id;
        private RequirementResponse requirement;
        private CandidateResponse candidate;
        private String status;
        private String notes;
        private String createdAt;
    }
}
