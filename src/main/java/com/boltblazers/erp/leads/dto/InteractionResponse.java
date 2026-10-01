package com.boltblazers.erp.leads.dto;

import com.boltblazers.erp.leads.InteractionOutcome;
import com.boltblazers.erp.leads.InteractionType;
import lombok.Data;
import java.time.Instant;

@Data
public class InteractionResponse {
    private Long id;
    private Long leadId;
    private InteractionType type;
    private InteractionOutcome outcome;
    private String summary;
    private Instant occurredAt;
    private Instant createdAt;
}
