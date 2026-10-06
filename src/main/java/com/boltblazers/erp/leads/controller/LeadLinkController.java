package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.LeadLinkRequest;
import com.boltblazers.erp.leads.dto.LeadLinkResponse;
import com.boltblazers.erp.leads.service.LeadLinkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadLinkController {

    private final LeadLinkService linkService;

    public LeadLinkController(LeadLinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/{leadId}/links")
    public ResponseEntity<LeadLinkResponse> addLink(@PathVariable Long leadId, @Valid @RequestBody LeadLinkRequest request) {
        return ResponseEntity.ok(linkService.addLink(leadId, request));
    }

    @GetMapping("/{leadId}/links")
    public ResponseEntity<List<LeadLinkResponse>> getLinks(@PathVariable Long leadId) {
        return ResponseEntity.ok(linkService.getLinksForLead(leadId));
    }

    @PutMapping("/links/{linkId}")
    public ResponseEntity<LeadLinkResponse> updateLink(@PathVariable Long linkId, @RequestBody LeadLinkRequest request) {
        return ResponseEntity.ok(linkService.updateLink(linkId, request));
    }

    @DeleteMapping("/links/{linkId}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long linkId) {
        linkService.deleteLink(linkId);
        return ResponseEntity.noContent().build();
    }
}
