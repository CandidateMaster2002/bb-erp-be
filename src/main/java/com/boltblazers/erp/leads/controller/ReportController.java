package com.boltblazers.erp.leads.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @GetMapping("/leads-by-stage")
    public ResponseEntity<?> getLeadsByStage(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/leads-by-source")
    public ResponseEntity<?> getLeadsBySource(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/leads-by-category")
    public ResponseEntity<?> getLeadsByCategory(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/conversion")
    public ResponseEntity<?> getConversion(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/avg-days-to-close")
    public ResponseEntity<?> getAvgDaysToClose(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/activity")
    public ResponseEntity<?> getActivity(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/followups")
    public ResponseEntity<?> getFollowupsReport(
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo) {
        return ResponseEntity.ok(Map.of());
    }
}
