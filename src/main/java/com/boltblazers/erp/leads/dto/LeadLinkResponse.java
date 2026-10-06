package com.boltblazers.erp.leads.dto;

import lombok.Data;

@Data
public class LeadLinkResponse {
    private Long id;
    private Long leadId;
    private String title;
    private String url;
    private String description;
    private String createdAt;
    private String updatedAt;
}
