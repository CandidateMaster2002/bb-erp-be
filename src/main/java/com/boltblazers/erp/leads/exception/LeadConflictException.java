package com.boltblazers.erp.leads.exception;

import lombok.Getter;

@Getter
public class LeadConflictException extends RuntimeException {
    private final Long leadId;
    private final String leadName;

    public LeadConflictException(Long leadId, String leadName) {
        super("Lead already exists with this phone number.");
        this.leadId = leadId;
        this.leadName = leadName;
    }
}
