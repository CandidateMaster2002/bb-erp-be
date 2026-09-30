package com.boltblazers.erp.leads.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @GetMapping("/summary")
    public ResponseEntity<?> getReminderSummary() {
        return ResponseEntity.ok(Map.of(
            "dueToday", 0,
            "overdue", 0,
            "dueIn60Mins", 0,
            "pendingCommitments", 0
        ));
    }

    @GetMapping("/due-now")
    public ResponseEntity<?> getDueNow() {
        return ResponseEntity.ok().build();
    }
}
