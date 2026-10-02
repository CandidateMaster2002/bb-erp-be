package com.boltblazers.erp.leads.dto;

import com.boltblazers.erp.leads.LeadPriority;
import lombok.Data;

@Data
public class LeadUpdateRequest {
    private String fullName;
    private String headline;
    private String summary;
    private String city;
    private String state;
    private String country;
    private String location;
    private String profilePictureUrl;
    
    // Flat convenience fields
    private String jobTitle;
    private String company;
    private String linkedinUrl;
    private String mobileNumber;
    private String personalEmail;
    
    private String remark;
    private LeadPriority priority;
}
