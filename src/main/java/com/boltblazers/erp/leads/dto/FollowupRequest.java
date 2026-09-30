package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Data
public class FollowupRequest {
    @NotNull private Instant dueAt;
    private String note;
}
