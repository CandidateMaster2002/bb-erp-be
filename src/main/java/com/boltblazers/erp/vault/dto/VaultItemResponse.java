package com.boltblazers.erp.vault.dto;

import lombok.Data;

@Data
public class VaultItemResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String link;
    private Boolean isArchived;
    private String createdAt;
    private String updatedAt;
}
