package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LeadSearchCriteria {
    private String q;
    private Long categoryId;
    private Long excludeCategoryId;
    private Long tagId;
    private String priority;
    private String source;
    private String city;
    private Instant followupFrom;
    private Instant followupTo;
    private Integer notContactedDays;
    private Boolean hasNoFollowup;
    private Boolean hasMobileNo;

    public void setSearch(String search) {
        this.q = search;
    }
}
