package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lead_education")
@Data
public class LeadEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    private String college;
    
    private String degree;
    
    private String branch;

    @Column(name = "batch_start")
    private String batchStart;

    @Column(name = "batch_end")
    private String batchEnd;
}
