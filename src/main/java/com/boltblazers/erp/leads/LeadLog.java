package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "lead_logs")
@Data
public class LeadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    private String comment;

    @Column(name = "next_action")
    private String nextAction;

    @Column(name = "next_action_date")
    private LocalDate nextActionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_status")
    private ActionStatus actionStatus = ActionStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
