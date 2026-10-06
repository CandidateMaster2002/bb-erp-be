package com.boltblazers.erp.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class TaskRecurringRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotEmpty(message = "At least one day of week is required (e.g. MONDAY, TUESDAY, or ALL)")
    private List<String> daysOfWeek;
    
    // Optional HH:mm formatted string (e.g., "10:00")
    private String timeOfDay;
    
    // Optional end date (YYYY-MM-DD), defaults to 1 year from now
    private String endDate;
}
