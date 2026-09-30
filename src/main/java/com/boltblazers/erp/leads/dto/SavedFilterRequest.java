package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavedFilterRequest {
    @NotBlank private String name;
    @NotBlank private String filterJson;
}
