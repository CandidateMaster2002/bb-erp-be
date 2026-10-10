package com.boltblazers.erp.tasks.dto;

import lombok.Data;

@Data
public class SubActionResponse {
    private Long id;
    private Long taskId;
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private String createdAt;
    private String updatedAt;
}
