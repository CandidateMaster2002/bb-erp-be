package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.SavedFilterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saved-filters")
public class SavedFilterController {

    @PostMapping
    public ResponseEntity<?> createSavedFilter(@Valid @RequestBody SavedFilterRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getSavedFilters() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedFilter(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
