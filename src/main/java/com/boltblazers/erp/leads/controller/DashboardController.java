package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.DashboardResponse;
import com.boltblazers.erp.leads.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
    
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Support both /api/dashboard and /api/leads/dashboard/today (frontend uses the latter)
    @GetMapping({"/api/dashboard", "/api/leads/dashboard/today"})
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }
}
