package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leads")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"categories", "tags"})
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String headline;
    
    private String summary;
    
    private String city;
    
    private String state;
    
    private String country;
    
    private String location;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name = "linkedin_url")
    private String linkedinUrl;
    
    @Column(name = "company_name")
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private LeadPriority priority;

    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_id")
    private Import sourceImport;

    @Column(name = "dedup_key_used")
    private String dedupKeyUsed;

    private String remark;

    @Column(name = "next_followup_at")
    private Instant nextFollowupAt;

    @Column(name = "last_contacted_at")
    private Instant lastContactedAt;

    @Column(name = "lost_reason")
    private String lostReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany
    @JoinTable(
        name = "lead_categories",
        joinColumns = @JoinColumn(name = "lead_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "lead_tags",
        joinColumns = @JoinColumn(name = "lead_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();
}
