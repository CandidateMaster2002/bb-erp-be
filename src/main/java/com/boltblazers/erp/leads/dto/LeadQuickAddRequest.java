package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadQuickAddRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String phone;

    private String notes;

    private String categoryId;
    private String priority;
    
    private String jobTitle;
    private String company;
    private String linkedinUrl;
    private String profilePictureUrl;
}
