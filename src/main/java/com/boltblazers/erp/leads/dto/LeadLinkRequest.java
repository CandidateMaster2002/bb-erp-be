package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadLinkRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "URL is required")
    private String url;
    
    private String description;
}
