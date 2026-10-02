package com.boltblazers.erp.leads.controller;

import com.boltblazers.erp.leads.dto.CategoryGroupResponse;
import com.boltblazers.erp.leads.dto.CategoryRequest;
import com.boltblazers.erp.leads.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MasterDataController {

    private final CategoryService categoryService;

    public MasterDataController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ==================== CATEGORY GROUPS ====================

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryGroupResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllGroups());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryGroupResponse> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getGroup(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryGroupResponse> createCategoryGroup(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createGroup(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryGroupResponse> updateCategoryGroup(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateGroup(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryGroup(@PathVariable Long id) {
        categoryService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CATEGORY VALUES ====================

    @PostMapping("/categories/{groupId}/values")
    public ResponseEntity<CategoryGroupResponse.CategoryValueResponse> addCategoryValue(
            @PathVariable Long groupId, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.addValue(groupId, request));
    }

    @PutMapping("/categories/values/{valueId}")
    public ResponseEntity<CategoryGroupResponse.CategoryValueResponse> updateCategoryValue(
            @PathVariable Long valueId, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateValue(valueId, request));
    }

    @DeleteMapping("/categories/values/{valueId}")
    public ResponseEntity<Void> deleteCategoryValue(@PathVariable Long valueId) {
        categoryService.deleteValue(valueId);
        return ResponseEntity.noContent().build();
    }
}
