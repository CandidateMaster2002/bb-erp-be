package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.LeadLogRequest;
import com.boltblazers.erp.leads.dto.LeadLogResponse;
import com.boltblazers.erp.leads.service.LeadLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LeadLogController {

    private final LeadLogService logService;

    public LeadLogController(LeadLogService logService) {
        this.logService = logService;
    }

    // ========== Per-Lead Log CRUD ==========

    @PostMapping("/leads/{leadId}/logs")
    public ResponseEntity<LeadLogResponse> addLog(@PathVariable Long leadId, @RequestBody LeadLogRequest request) {
        return ResponseEntity.ok(logService.addLog(leadId, request));
    }

    @GetMapping("/leads/{leadId}/logs")
    public ResponseEntity<List<LeadLogResponse>> getLogsForLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(logService.getLogsForLead(leadId));
    }

    @PutMapping("/leads/logs/{logId}")
    public ResponseEntity<LeadLogResponse> updateLog(@PathVariable Long logId, @RequestBody LeadLogRequest request) {
        return ResponseEntity.ok(logService.updateLog(logId, request));
    }

    @DeleteMapping("/leads/logs/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId) {
        logService.deleteLog(logId);
        return ResponseEntity.noContent().build();
    }

    // ========== Mark Complete / Cancel ==========

    @PatchMapping("/leads/logs/{logId}/complete")
    public ResponseEntity<LeadLogResponse> markCompleted(@PathVariable Long logId) {
        return ResponseEntity.ok(logService.markCompleted(logId));
    }

    @PatchMapping("/leads/logs/{logId}/cancel")
    public ResponseEntity<LeadLogResponse> markCancelled(@PathVariable Long logId) {
        return ResponseEntity.ok(logService.markCancelled(logId));
    }

    // ========== Actions View (date-wise agenda, only PENDING) ==========

    @GetMapping("/actions/today")
    public ResponseEntity<List<LeadLogResponse>> getTodayActions() {
        return ResponseEntity.ok(logService.getOverdueAndTodayActions());
    }

    @GetMapping("/actions")
    public ResponseEntity<List<LeadLogResponse>> getActionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(logService.getActionsForDate(date));
    }

    @GetMapping("/actions/range")
    public ResponseEntity<List<LeadLogResponse>> getActionsInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(logService.getActionsBetween(from, to));
    }
}
