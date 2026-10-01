package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.FollowupRequest;
import com.boltblazers.erp.leads.dto.FollowupResponse;
import com.boltblazers.erp.leads.dto.SnoozeRequest;
import com.boltblazers.erp.leads.service.FollowupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FollowupController {
    
    private final FollowupService followupService;
    
    public FollowupController(FollowupService followupService) {
        this.followupService = followupService;
    }
    
    @PostMapping("/leads/{id}/followups")
    public ResponseEntity<FollowupResponse> createFollowup(@PathVariable Long id, @Valid @RequestBody FollowupRequest request) {
        return ResponseEntity.ok(followupService.createFollowup(id, request));
    }

    @PatchMapping("/followups/{id}/done")
    public ResponseEntity<?> markDone(@PathVariable Long id) {
        followupService.markDone(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/followups/{id}/snooze")
    public ResponseEntity<?> snooze(@PathVariable Long id, @Valid @RequestBody SnoozeRequest request) {
        return ResponseEntity.ok().build(); // TODO
    }

    @GetMapping("/followups/today")
    public ResponseEntity<?> getToday() {
        return ResponseEntity.ok().build(); // TODO
    }
}
