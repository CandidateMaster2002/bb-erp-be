package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "commitments")
@Data
public class Commitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Column(nullable = false)
    private String item;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CommitmentStatus status;

    @Column(name = "file_or_link")
    private String fileOrLink;

    @Column(name = "sent_at")
    private Instant sentAt;
}
