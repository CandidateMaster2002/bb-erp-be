package com.boltblazers.erp.leads.dto;

import lombok.Data;

@Data
public class LeadLogResponse {
    private Long id;
    private Long leadId;
    private String leadName;
    private String comment;
    private String nextAction;
    private String nextActionDate;
    private String actionStatus;
    private String createdAt;
}
