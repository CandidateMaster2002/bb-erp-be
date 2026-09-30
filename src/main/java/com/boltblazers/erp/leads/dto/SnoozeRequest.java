package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Data
public class SnoozeRequest {
    @NotNull private Instant dueAt;
}
