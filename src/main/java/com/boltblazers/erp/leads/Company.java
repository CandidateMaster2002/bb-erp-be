package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "linkedin_id")
    private String linkedinId;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    private String website;
    
    private String industry;
    
    private String employees;

    @Column(name = "founded_year")
    private Integer foundedYear;
    
    private String location;
    
    private String specialities;
    
    private String description;
}
