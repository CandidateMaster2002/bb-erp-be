package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lead_jobs")
@Data
public class LeadJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "started_on")
    private String startedOn;

    @Column(name = "is_current")
    private Boolean isCurrent = false;

    @Column(name = "recently_hired")
    private Boolean recentlyHired = false;
}
