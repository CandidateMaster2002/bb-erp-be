package com.boltblazers.erp.vault;

import com.boltblazers.erp.tasks.dto.TaskRequest;
import com.boltblazers.erp.tasks.dto.TaskResponse;
import com.boltblazers.erp.tasks.service.TaskService;
import com.boltblazers.erp.vault.dto.PromoteVaultRequest;
import com.boltblazers.erp.vault.dto.VaultItemRequest;
import com.boltblazers.erp.vault.dto.VaultItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VaultService {

    private final VaultItemRepository vaultItemRepository;
    private final TaskService taskService;

    public VaultService(VaultItemRepository vaultItemRepository, TaskService taskService) {
        this.vaultItemRepository = vaultItemRepository;
        this.taskService = taskService;
    }

    public VaultItemResponse createVaultItem(VaultItemRequest request) {
        VaultItem item = new VaultItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            item.setCategory(request.getCategory().toUpperCase());
        } else {
            item.setCategory("IDEA");
        }
        item.setLink(request.getLink());
        item.setIsArchived(Boolean.TRUE.equals(request.getIsArchived()));
        return toResponse(vaultItemRepository.save(item));
    }

    public List<VaultItemResponse> getVaultItems(String category, Boolean includeArchived) {
        List<VaultItem> items;
        boolean incArchived = Boolean.TRUE.equals(includeArchived);

        if (category != null && !category.isBlank()) {
            String cat = category.toUpperCase();
            if (incArchived) {
                items = vaultItemRepository.findAllByOrderByCreatedAtDesc().stream()
                        .filter(i -> cat.equals(i.getCategory()))
                        .collect(Collectors.toList());
            } else {
                items = vaultItemRepository.findByCategoryAndIsArchivedOrderByCreatedAtDesc(cat, false);
            }
        } else {
            if (incArchived) {
                items = vaultItemRepository.findAllByOrderByCreatedAtDesc();
            } else {
                items = vaultItemRepository.findByIsArchivedOrderByCreatedAtDesc(false);
            }
        }

        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public VaultItemResponse getVaultItem(Long id) {
        return toResponse(vaultItemRepository.findById(id).orElseThrow());
    }

    public VaultItemResponse updateVaultItem(Long id, VaultItemRequest request) {
        VaultItem item = vaultItemRepository.findById(id).orElseThrow();
        if (request.getTitle() != null) item.setTitle(request.getTitle());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            item.setCategory(request.getCategory().toUpperCase());
        }
        if (request.getLink() != null) item.setLink(request.getLink());
        if (request.getIsArchived() != null) item.setIsArchived(request.getIsArchived());
        return toResponse(vaultItemRepository.save(item));
    }

    public void deleteVaultItem(Long id) {
        vaultItemRepository.deleteById(id);
    }

    public VaultItemResponse toggleArchive(Long id, Boolean archived) {
        VaultItem item = vaultItemRepository.findById(id).orElseThrow();
        item.setIsArchived(archived != null ? archived : !item.getIsArchived());
        return toResponse(vaultItemRepository.save(item));
    }

    public TaskResponse promoteToTask(Long id, PromoteVaultRequest request) {
        VaultItem item = vaultItemRepository.findById(id).orElseThrow();

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle(item.getTitle());

        StringBuilder desc = new StringBuilder();
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            desc.append(item.getDescription());
        }
        if (item.getLink() != null && !item.getLink().isBlank()) {
            if (desc.length() > 0) desc.append("\n\n");
            desc.append("Link: ").append(item.getLink());
        }
        taskRequest.setDescription(desc.length() > 0 ? desc.toString() : null);

        if (request != null && request.getTimeHorizon() != null && !request.getTimeHorizon().isBlank()) {
            taskRequest.setTimeHorizon(request.getTimeHorizon().toUpperCase());
        } else {
            taskRequest.setTimeHorizon("TODAY");
        }

        if (request != null && request.getDeadline() != null && !request.getDeadline().isBlank()) {
            taskRequest.setDeadline(request.getDeadline());
        }

        TaskResponse createdTask = taskService.addTask(taskRequest);

        if (request == null || !Boolean.FALSE.equals(request.getArchiveVaultItem())) {
            item.setIsArchived(true);
            vaultItemRepository.save(item);
        }

        return createdTask;
    }

    private VaultItemResponse toResponse(VaultItem item) {
        VaultItemResponse res = new VaultItemResponse();
        res.setId(item.getId());
        res.setTitle(item.getTitle());
        res.setDescription(item.getDescription());
        res.setCategory(item.getCategory());
        res.setLink(item.getLink());
        res.setIsArchived(item.getIsArchived());
        if (item.getCreatedAt() != null) res.setCreatedAt(item.getCreatedAt().toString());
        if (item.getUpdatedAt() != null) res.setUpdatedAt(item.getUpdatedAt().toString());
        return res;
    }
}
