package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LeadSearchCriteria {
    private String q;
    private Long categoryId;
    private Long excludeCategoryId;
    private Long tagId;
    private java.util.List<Long> tagIds;
    private String priority;
    private String source;
    private String city;
    private String recordType = "LEAD";
    private Instant followupFrom;
    private Instant followupTo;
    private Integer notContactedDays;
    private Boolean hasNoFollowup;
    private Boolean hasMobileNo;
    private Boolean mobileUpdatedToday;

    public void setSearch(String search) {
        this.q = search;
    }
}
