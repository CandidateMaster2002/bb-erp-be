package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lead_contacts")
@Data
public class LeadContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ContactType type;

    @Column(nullable = false)
    private String value;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
}
