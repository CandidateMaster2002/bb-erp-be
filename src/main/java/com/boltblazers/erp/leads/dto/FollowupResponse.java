package com.boltblazers.erp.leads.dto;

import com.boltblazers.erp.leads.FollowupStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class FollowupResponse {
    private Long id;
    private Long leadId;
    private Instant dueAt;
    private String note;
    private FollowupStatus status;
    private Integer recurrenceDays;
    private Instant completedAt;
    private Instant createdAt;
}
