package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.CommitmentRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommitmentController {
    
    @PostMapping("/leads/{id}/commitments")
    public ResponseEntity<?> createCommitment(@PathVariable Long id, @Valid @RequestBody CommitmentRequest request) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/commitments/{id}/sent")
    public ResponseEntity<?> markSent(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/commitments/pending")
    public ResponseEntity<?> getPending() {
        return ResponseEntity.ok().build();
    }
}
