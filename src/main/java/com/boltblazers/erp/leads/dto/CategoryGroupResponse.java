package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryGroupResponse {
    private Long id;
    private String name;
    private List<CategoryValueResponse> values;

    @Data
    public static class CategoryValueResponse {
        private Long id;
        private String name;
    }
}
