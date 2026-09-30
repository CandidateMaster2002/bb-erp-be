package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stages")
@Data
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
