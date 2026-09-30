package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.InteractionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InteractionController {
    
    @PostMapping("/leads/{id}/interactions")
    public ResponseEntity<?> logInteraction(@PathVariable Long id, @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.ok().build(); // TODO implement service
    }

    @GetMapping("/leads/{id}/interactions")
    public ResponseEntity<?> getInteractions(@PathVariable Long id) {
        return ResponseEntity.ok().build(); // TODO implement service
    }

    @PutMapping("/interactions/{id}")
    public ResponseEntity<?> updateInteraction(@PathVariable Long id, @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/interactions/{id}")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
