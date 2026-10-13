package com.boltblazers.erp.vault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VaultItemRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String category; // IDEA, ASPIRATION, TO_BUY, NOTE

    private String link;

    private Boolean isArchived;
}
