package com.boltblazers.erp.staffing.controller;

import com.boltblazers.erp.staffing.dto.StaffingDtos.*;
import com.boltblazers.erp.staffing.service.StaffingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staffing")
public class StaffingController {

    private final StaffingService service;

    public StaffingController(StaffingService service) {
        this.service = service;
    }

    // ==================== DEMAND SOURCES (CLIENTS) ====================
    @PostMapping("/clients")
    public ResponseEntity<DemandSourceResponse> createClient(@RequestBody DemandSourceRequest req) {
        return ResponseEntity.ok(service.createDemandSource(req));
    }
    @GetMapping("/clients")
    public ResponseEntity<List<DemandSourceResponse>> getClients() {
        return ResponseEntity.ok(service.getAllDemandSources());
    }
    @PutMapping("/clients/{id}")
    public ResponseEntity<DemandSourceResponse> updateClient(@PathVariable Long id, @RequestBody DemandSourceRequest req) {
        return ResponseEntity.ok(service.updateDemandSource(id, req));
    }
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.deleteDemandSource(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== SUPPLY SOURCES (VENDORS) ====================
    @PostMapping("/vendors")
    public ResponseEntity<SupplySourceResponse> createVendor(@RequestBody SupplySourceRequest req) {
        return ResponseEntity.ok(service.createSupplySource(req));
    }
    @GetMapping("/vendors")
    public ResponseEntity<List<SupplySourceResponse>> getVendors() {
        return ResponseEntity.ok(service.getAllSupplySources());
    }
    @PutMapping("/vendors/{id}")
    public ResponseEntity<SupplySourceResponse> updateVendor(@PathVariable Long id, @RequestBody SupplySourceRequest req) {
        return ResponseEntity.ok(service.updateSupplySource(id, req));
    }
    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        service.deleteSupplySource(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== REQUIREMENTS (JOB ORDERS) ====================
    @PostMapping("/requirements")
    public ResponseEntity<RequirementResponse> createRequirement(@RequestBody RequirementRequest req) {
        return ResponseEntity.ok(service.createRequirement(req));
    }
    @GetMapping("/requirements")
    public ResponseEntity<List<RequirementResponse>> getRequirements() {
        return ResponseEntity.ok(service.getAllRequirements());
    }
    @PutMapping("/requirements/{id}")
    public ResponseEntity<RequirementResponse> updateRequirement(@PathVariable Long id, @RequestBody RequirementRequest req) {
        return ResponseEntity.ok(service.updateRequirement(id, req));
    }
    @DeleteMapping("/requirements/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        service.deleteRequirement(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CANDIDATES ====================
    @PostMapping("/candidates")
    public ResponseEntity<CandidateResponse> createCandidate(@RequestBody CandidateRequest req) {
        return ResponseEntity.ok(service.createCandidate(req));
    }
    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateResponse>> getCandidates() {
        return ResponseEntity.ok(service.getAllCandidates());
    }
    @PutMapping("/candidates/{id}")
    public ResponseEntity<CandidateResponse> updateCandidate(@PathVariable Long id, @RequestBody CandidateRequest req) {
        return ResponseEntity.ok(service.updateCandidate(id, req));
    }
    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        service.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== SUBMISSIONS ====================
    @PostMapping("/submissions")
    public ResponseEntity<SubmissionResponse> createSubmission(@RequestBody SubmissionRequest req) {
        return ResponseEntity.ok(service.createSubmission(req));
    }
    @GetMapping("/submissions")
    public ResponseEntity<List<SubmissionResponse>> getSubmissions() {
        return ResponseEntity.ok(service.getAllSubmissions());
    }
    @PutMapping("/submissions/{id}")
    public ResponseEntity<SubmissionResponse> updateSubmission(@PathVariable Long id, @RequestBody SubmissionRequest req) {
        return ResponseEntity.ok(service.updateSubmission(id, req));
    }
    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        service.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}
