package com.boltblazers.erp.leads.dto;

import lombok.Data;

@Data
public class LeadLogRequest {
    private String comment;
    private String nextAction;
    private String nextActionDate; // yyyy-MM-dd
}
