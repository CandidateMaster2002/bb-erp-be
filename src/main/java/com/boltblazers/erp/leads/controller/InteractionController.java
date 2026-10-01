package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.InteractionRequest;
import com.boltblazers.erp.leads.dto.InteractionResponse;
import com.boltblazers.erp.leads.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InteractionController {
    
    private final InteractionService interactionService;
    
    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/leads/{id}/interactions")
    public ResponseEntity<InteractionResponse> logInteraction(@PathVariable Long id, @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.ok(interactionService.logInteraction(id, request));
    }

    @GetMapping("/leads/{id}/interactions")
    public ResponseEntity<?> getInteractions(@PathVariable Long id) {
        return ResponseEntity.ok().build(); // TODO: Return actual list
    }
}
