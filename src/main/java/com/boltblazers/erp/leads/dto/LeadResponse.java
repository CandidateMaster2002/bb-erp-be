package com.boltblazers.erp.leads.dto;

import com.boltblazers.erp.leads.LeadPriority;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class LeadResponse {
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String headline;
    private String summary;
    private String city;
    private String state;
    private String country;
    private String location;
    private String profilePictureUrl;
    private Long stageId;
    private String stageName;
    private LeadPriority priority;
    private String source;
    private String remark;
    private Instant nextFollowupAt;
    private Instant lastContactedAt;
    private String lostReason;
    private Instant createdAt;
    private Instant updatedAt;
    
    // Flat fields for convenience in lists
    private String jobTitle;
    private String company;
    private String mobileNumber;
    private String linkedinUrl;
    
    // Additional nested fields for the full profile
    private List<ContactDto> contacts;
    private LinkedinDto linkedin;
    private List<JobDto> jobs;
    private List<EducationDto> education;
    private List<CategoryDto> categories;
    private List<TagDto> tags;
    private List<CommitmentDto> pendingCommitments;
    
    @Data
    public static class ContactDto {
        private Long id;
        private String type;
        private String value;
        private Boolean isPrimary;
    }
    
    @Data
    public static class LinkedinDto {
        private String linkedinUrl;
        private Integer followersCount;
    }
    
    @Data
    public static class JobDto {
        private Long id;
        private String jobTitle;
        private String companyName;
        private Boolean isCurrent;
    }
    
    @Data
    public static class EducationDto {
        private Long id;
        private String college;
        private String degree;
    }
    
    @Data
    public static class CategoryDto {
        private Long id;
        private String name;
    }
    
    @Data
    public static class TagDto {
        private Long id;
        private String name;
    }
    
    @Data
    public static class CommitmentDto {
        private Long id;
        private String item;
        private String dueDate;
    }
}
