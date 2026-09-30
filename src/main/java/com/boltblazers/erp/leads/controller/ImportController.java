package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.ImportSummaryResponse;
import com.boltblazers.erp.leads.service.LeadImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/imports")
public class ImportController {

    private final LeadImportService leadImportService;

    public ImportController(LeadImportService leadImportService) {
        this.leadImportService = leadImportService;
    }

    @PostMapping
    public ResponseEntity<ImportSummaryResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(value = "stageId", required = false) Long stageId) {
            
        ImportSummaryResponse summary = leadImportService.processImport(file, categoryIds, stageId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping
    public ResponseEntity<?> getImports() {
        return ResponseEntity.ok(leadImportService.getAllImports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImport(@PathVariable Long id) {
        return ResponseEntity.ok(leadImportService.getImport(id));
    }
}
