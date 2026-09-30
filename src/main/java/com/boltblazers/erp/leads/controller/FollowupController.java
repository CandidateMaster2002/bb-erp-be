package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.FollowupRequest;
import com.boltblazers.erp.leads.dto.SnoozeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FollowupController {
    
    @PostMapping("/leads/{id}/followups")
    public ResponseEntity<?> createFollowup(@PathVariable Long id, @Valid @RequestBody FollowupRequest request) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/followups/{id}/done")
    public ResponseEntity<?> markDone(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/followups/{id}/snooze")
    public ResponseEntity<?> snooze(@PathVariable Long id, @Valid @RequestBody SnoozeRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followups/today")
    public ResponseEntity<?> getToday() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followups/overdue")
    public ResponseEntity<?> getOverdue() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followups/upcoming")
    public ResponseEntity<?> getUpcoming(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/followups/{id}/acknowledge")
    public ResponseEntity<?> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
