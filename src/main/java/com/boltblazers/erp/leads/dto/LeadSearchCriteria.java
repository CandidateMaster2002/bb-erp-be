package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LeadSearchCriteria {
    private String q;
    private Long stageId;
    private Long categoryId;
    private Long tagId;
    private String priority;
    private String source;
    private String city;
    private Instant followupFrom;
    private Instant followupTo;
    private Integer notContactedDays;
    private Boolean hasNoFollowup;

    public void setSearch(String search) {
        this.q = search;
    }
}
