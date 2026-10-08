package com.boltblazers.erp.staffing.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "requirements")
public class Requirement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_source_id", nullable = false)
    private DemandSource demandSource;

    @Column(nullable = false)
    private String title;

    @Column(name = "jd_text", columnDefinition = "TEXT")
    private String jdText;

    private String ctc;
    
    @Column(name = "notice_period")
    private String noticePeriod;
    
    @Column(name = "experience_range")
    private String experienceRange;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private Instant updatedAt;
}
