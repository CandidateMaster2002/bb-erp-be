package com.boltblazers.erp.tasks.controller;

import com.boltblazers.erp.leads.ActionStatus;
import com.boltblazers.erp.tasks.dto.TaskRequest;
import com.boltblazers.erp.tasks.dto.TaskResponse;
import com.boltblazers.erp.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.addTask(request));
    }

    @PostMapping("/recurring")
    public ResponseEntity<List<TaskResponse>> addRecurringTasks(@Valid @RequestBody com.boltblazers.erp.tasks.dto.TaskRecurringRequest request) {
        return ResponseEntity.ok(taskService.createRecurringTasks(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recurring/{groupId}")
    public ResponseEntity<Void> deleteRecurringGroup(@PathVariable String groupId) {
        taskService.deleteRecurringGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markCompleted(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TaskResponse> cancelTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markCancelled(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PostMapping("/{id}/subactions")
    public ResponseEntity<com.boltblazers.erp.tasks.dto.SubActionResponse> addSubAction(
            @PathVariable Long id, @Valid @RequestBody com.boltblazers.erp.tasks.dto.SubActionRequest request) {
        return ResponseEntity.ok(taskService.addSubAction(id, request));
    }

    @PutMapping("/subactions/{subId}")
    public ResponseEntity<com.boltblazers.erp.tasks.dto.SubActionResponse> updateSubAction(
            @PathVariable Long subId, @RequestBody com.boltblazers.erp.tasks.dto.SubActionRequest request) {
        return ResponseEntity.ok(taskService.updateSubAction(subId, request));
    }

    @PatchMapping("/subactions/{subId}/status")
    public ResponseEntity<com.boltblazers.erp.tasks.dto.SubActionResponse> changeSubActionStatus(
            @PathVariable Long subId, @RequestParam String status) {
        return ResponseEntity.ok(taskService.changeSubActionStatus(subId, status));
    }

    @DeleteMapping("/subactions/{subId}")
    public ResponseEntity<Void> deleteSubAction(@PathVariable Long subId) {
        taskService.deleteSubAction(subId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public ResponseEntity<List<TaskResponse>> getTodayTasks() {
        return ResponseEntity.ok(taskService.getOverdueAndTodayActions());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TaskResponse>> getTasksForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(taskService.getActionsForDate(date));
    }

    @GetMapping("/between")
    public ResponseEntity<List<TaskResponse>> getTasksBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(taskService.getActionsBetween(from, to));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable ActionStatus status) {
        return ResponseEntity.ok(taskService.getByStatus(status));
    }
}
