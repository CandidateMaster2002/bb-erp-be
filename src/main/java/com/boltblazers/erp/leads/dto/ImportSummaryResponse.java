package com.boltblazers.erp.leads.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImportSummaryResponse {
    private Long importId;
    private int totalRows;
    private int created;
    private int updated;
    private int skipped;
    private List<RowError> errors;

    @Data
    public static class RowError {
        private int rowNumber;
        private String errorMessage;
        
        public RowError(int rowNumber, String errorMessage) {
            this.rowNumber = rowNumber;
            this.errorMessage = errorMessage;
        }
    }
}
