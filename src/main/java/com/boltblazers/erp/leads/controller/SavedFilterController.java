package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.SavedFilter;
import com.boltblazers.erp.leads.SavedFilterRepository;
import com.boltblazers.erp.leads.dto.SavedFilterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-filters")
public class SavedFilterController {

    private final SavedFilterRepository savedFilterRepository;

    public SavedFilterController(SavedFilterRepository savedFilterRepository) {
        this.savedFilterRepository = savedFilterRepository;
    }

    @PostMapping
    public ResponseEntity<SavedFilter> createSavedFilter(@Valid @RequestBody SavedFilterRequest request) {
        SavedFilter filter = new SavedFilter();
        filter.setName(request.getName());
        filter.setFilterJson(request.getFilterJson());
        filter.setUserId(1L);
        return ResponseEntity.ok(savedFilterRepository.save(filter));
    }

    @GetMapping
    public ResponseEntity<List<SavedFilter>> getSavedFilters() {
        return ResponseEntity.ok(savedFilterRepository.findAllByOrderByCreatedAtDesc());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedFilter(@PathVariable Long id) {
        savedFilterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
