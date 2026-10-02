package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class LeadQuickAddRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String phone;
    private String personalEmail;
    private String notes;

    private List<Long> categoryIds;
    private String priority;
    
    private String jobTitle;
    private String company;
    private String linkedinUrl;
    private String profilePictureUrl;

    private String city;
    private String state;
    private String country;
    private String location;
    private String headline;
    private String summary;
}
