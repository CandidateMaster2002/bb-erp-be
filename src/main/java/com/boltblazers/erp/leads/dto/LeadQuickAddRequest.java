package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadQuickAddRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phone;

    private String note;
}
