package com.boltblazers.erp.links.dto;

import lombok.Data;

@Data
public class GlobalLinkResponse {
    private Long id;
    private String title;
    private String url;
    private String description;
    private String createdAt;
    private String updatedAt;
}
