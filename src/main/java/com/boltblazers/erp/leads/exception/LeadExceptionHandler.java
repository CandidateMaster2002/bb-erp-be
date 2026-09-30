package com.boltblazers.erp.leads.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class LeadExceptionHandler {

    @ExceptionHandler(LeadConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(LeadConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "error", ex.getMessage(),
            "leadId", ex.getLeadId(),
            "leadName", ex.getLeadName() != null ? ex.getLeadName() : ""
        ));
    }
}
