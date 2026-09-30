package com.boltblazers.erp.leads;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "imports")
@Data
public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @CreationTimestamp
    @Column(name = "imported_at", nullable = false, updatable = false)
    private Instant importedAt;

    @Column(name = "total_rows", nullable = false)
    private Integer totalRows = 0;

    private String notes;
}
