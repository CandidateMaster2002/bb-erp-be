package com.boltblazers.erp.vault.dto;

import lombok.Data;

@Data
public class PromoteVaultRequest {
    private String timeHorizon = "TODAY"; // TODAY, THIS_WEEK, THIS_MONTH, SOMEDAY
    private String deadline;
    private Boolean archiveVaultItem = true;
}
