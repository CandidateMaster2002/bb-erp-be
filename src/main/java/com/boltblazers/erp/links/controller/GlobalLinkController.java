package com.boltblazers.erp.links.controller;

import com.boltblazers.erp.links.dto.GlobalLinkRequest;
import com.boltblazers.erp.links.dto.GlobalLinkResponse;
import com.boltblazers.erp.links.service.GlobalLinkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
public class GlobalLinkController {

    private final GlobalLinkService linkService;

    public GlobalLinkController(GlobalLinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<GlobalLinkResponse> addLink(@Valid @RequestBody GlobalLinkRequest request) {
        return ResponseEntity.ok(linkService.addLink(request));
    }

    @GetMapping
    public ResponseEntity<List<GlobalLinkResponse>> getAllLinks() {
        return ResponseEntity.ok(linkService.getAllLinks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalLinkResponse> updateLink(@PathVariable Long id, @RequestBody GlobalLinkRequest request) {
        return ResponseEntity.ok(linkService.updateLink(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}
