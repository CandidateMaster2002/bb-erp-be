package com.boltblazers.erp.tasks.dto;

import lombok.Data;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String deadline;
    private String status;
    private String recurrenceGroupId;
    private String timeHorizon;
    private String createdAt;
    private String updatedAt;
    private java.util.List<SubActionResponse> subactions;
}
