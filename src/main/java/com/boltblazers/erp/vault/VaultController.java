package com.boltblazers.erp.vault;

import com.boltblazers.erp.tasks.dto.TaskResponse;
import com.boltblazers.erp.vault.dto.PromoteVaultRequest;
import com.boltblazers.erp.vault.dto.VaultItemRequest;
import com.boltblazers.erp.vault.dto.VaultItemResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vault")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping
    public ResponseEntity<VaultItemResponse> createVaultItem(@Valid @RequestBody VaultItemRequest request) {
        return ResponseEntity.ok(vaultService.createVaultItem(request));
    }

    @GetMapping
    public ResponseEntity<List<VaultItemResponse>> getVaultItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") Boolean includeArchived) {
        return ResponseEntity.ok(vaultService.getVaultItems(category, includeArchived));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaultItemResponse> getVaultItem(@PathVariable Long id) {
        return ResponseEntity.ok(vaultService.getVaultItem(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaultItemResponse> updateVaultItem(
            @PathVariable Long id,
            @Valid @RequestBody VaultItemRequest request) {
        return ResponseEntity.ok(vaultService.updateVaultItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaultItem(@PathVariable Long id) {
        vaultService.deleteVaultItem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<VaultItemResponse> toggleArchive(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean archived) {
        return ResponseEntity.ok(vaultService.toggleArchive(id, archived));
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<TaskResponse> promoteToTask(
            @PathVariable Long id,
            @RequestBody(required = false) PromoteVaultRequest request) {
        return ResponseEntity.ok(vaultService.promoteToTask(id, request));
    }
}
