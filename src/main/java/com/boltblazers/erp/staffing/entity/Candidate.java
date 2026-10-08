package com.boltblazers.erp.staffing.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "candidates")
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_source_id")
    private SupplySource supplySource;

    @Column(nullable = false)
    private String name;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "resume_link", columnDefinition = "TEXT")
    private String resumeLink;

    @Column(name = "current_ctc")
    private String currentCtc;

    @Column(name = "expected_ctc")
    private String expectedCtc;

    @Column(name = "notice_period")
    private String noticePeriod;

    private String gap;

    @Column(name = "college_tier")
    private String collegeTier;

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private Instant updatedAt;
}
