package com.boltblazers.erp.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubActionRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String dueDate;
}
