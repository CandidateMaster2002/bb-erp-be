package com.boltblazers.erp.leads.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MasterDataController {
    
    @GetMapping("/stages")
    public ResponseEntity<?> getStages() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTags() {
        return ResponseEntity.ok().build();
    }
}
