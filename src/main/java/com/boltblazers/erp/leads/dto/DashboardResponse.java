package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardResponse {
    private long todayFollowups;
    private long overdueFollowups;
    private long pendingCommitments;
    private long newLeadsThisWeek;
    private Map<String, Long> leadsByStage;
}
