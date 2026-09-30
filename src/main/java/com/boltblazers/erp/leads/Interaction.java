package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "interactions")
@Data
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InteractionType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private InteractionOutcome outcome;

    private String summary;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
