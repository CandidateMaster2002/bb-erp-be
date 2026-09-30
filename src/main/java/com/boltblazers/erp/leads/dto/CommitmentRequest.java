package com.boltblazers.erp.leads.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CommitmentRequest {
    @NotBlank private String item;
    private LocalDate dueDate;
    private String fileOrLink;
}
