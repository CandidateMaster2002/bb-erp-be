package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lead_linkedin")
@Data
public class LeadLinkedin {

    @Id
    private Long leadId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "member_urn")
    private String memberUrn;

    @Column(name = "public_identifier")
    private String publicIdentifier;

    @Column(name = "followers_count")
    private Integer followersCount;

    private Boolean premium;

    @Column(name = "personal_website")
    private String personalWebsite;
}
