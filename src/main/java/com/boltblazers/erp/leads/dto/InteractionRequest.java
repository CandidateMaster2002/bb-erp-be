package com.boltblazers.erp.leads.dto;

import com.boltblazers.erp.leads.InteractionOutcome;
import com.boltblazers.erp.leads.InteractionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Data
public class InteractionRequest {
    @NotNull private InteractionType type;
    private InteractionOutcome outcome;
    private String summary;
    private Instant occurredAt;
    private Instant nextFollowupAt;
}
