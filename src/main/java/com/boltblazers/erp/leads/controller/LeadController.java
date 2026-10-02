package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.*;
import com.boltblazers.erp.leads.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    public ResponseEntity<LeadResponse> quickAdd(@Valid @RequestBody LeadQuickAddRequest request) {
        return ResponseEntity.ok(leadService.quickAdd(request));
    }

    @GetMapping
    public ResponseEntity<Page<LeadResponse>> list(LeadSearchCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(leadService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leadService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> updateLead(@PathVariable Long id, @RequestBody LeadUpdateRequest request) {
        return ResponseEntity.ok(leadService.updateLead(id, request));
    }

    @PutMapping("/{id}/categories")
    public ResponseEntity<LeadResponse> updateCategories(@PathVariable Long id, @RequestBody java.util.List<Long> categoryIds) {
        return ResponseEntity.ok(leadService.updateCategories(id, categoryIds));
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<?> updateTags(@PathVariable Long id, @RequestBody java.util.List<Long> tagIds) {
        return ResponseEntity.ok().build();
    }
}
