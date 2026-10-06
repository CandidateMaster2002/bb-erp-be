package com.boltblazers.erp.tasks.service;

import com.boltblazers.erp.leads.ActionStatus;
import com.boltblazers.erp.tasks.Task;
import com.boltblazers.erp.tasks.TaskRepository;
import com.boltblazers.erp.tasks.dto.TaskRequest;
import com.boltblazers.erp.tasks.dto.TaskResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private Instant parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        dateStr = dateStr.trim();
        if (dateStr.length() == 10) {
            return LocalDate.parse(dateStr).atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
        try {
            if (dateStr.endsWith("Z") || dateStr.contains("+") || dateStr.indexOf("-", 10) != -1) {
                return Instant.parse(dateStr);
            }
            return java.time.LocalDateTime.parse(dateStr).atZone(ZoneId.of("UTC")).toInstant();
        } catch (Exception e) {
            return Instant.parse(dateStr);
        }
    }

    public TaskResponse addTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(parseDate(request.getDeadline()));
        task.setStatus(ActionStatus.PENDING);
        taskRepository.save(task);
        return toResponse(task);
    }

    public List<TaskResponse> createRecurringTasks(com.boltblazers.erp.tasks.dto.TaskRecurringRequest request) {
        String groupId = java.util.UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        if (request.getEndDate() != null && !request.getEndDate().isBlank()) {
            endDate = LocalDate.parse(request.getEndDate());
        }
        
        java.time.LocalTime time = java.time.LocalTime.of(0, 0);
        if (request.getTimeOfDay() != null && !request.getTimeOfDay().isBlank()) {
            time = java.time.LocalTime.parse(request.getTimeOfDay());
        }

        List<String> days = request.getDaysOfWeek().stream().map(String::toUpperCase).collect(Collectors.toList());
        boolean allDays = days.contains("ALL");

        List<Task> tasksToSave = new java.util.ArrayList<>();
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (allDays || days.contains(date.getDayOfWeek().name())) {
                Task task = new Task();
                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setDeadline(date.atTime(time).atZone(ZoneId.of("UTC")).toInstant());
                task.setStatus(ActionStatus.PENDING);
                task.setRecurrenceGroupId(groupId);
                tasksToSave.add(task);
            }
        }
        
        taskRepository.saveAll(tasksToSave);
        return tasksToSave.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDeadline() != null) task.setDeadline(parseDate(request.getDeadline()));
        taskRepository.save(task);
        return toResponse(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void deleteRecurringGroup(String groupId) {
        List<Task> tasks = taskRepository.findAll().stream()
            .filter(t -> groupId.equals(t.getRecurrenceGroupId()))
            .collect(Collectors.toList());
        taskRepository.deleteAll(tasks);
    }

    public TaskResponse markCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(ActionStatus.COMPLETED);
        taskRepository.save(task);
        return toResponse(task);
    }

    public TaskResponse markCancelled(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(ActionStatus.CANCELLED);
        taskRepository.save(task);
        return toResponse(task);
    }

    public List<TaskResponse> getActionsForDate(LocalDate date) {
        Instant start = date.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return taskRepository.findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus.PENDING, start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> getOverdueAndTodayActions() {
        LocalDate today = LocalDate.now();
        Instant end = today.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return taskRepository.findByStatusAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus.PENDING, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> getActionsBetween(LocalDate from, LocalDate to) {
        Instant start = from.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = to.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return taskRepository.findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus.PENDING, start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    public List<TaskResponse> getByStatus(ActionStatus status) {
        return taskRepository.findByStatusOrderByDeadlineAsc(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        if (task.getDeadline() != null) res.setDeadline(task.getDeadline().toString());
        res.setStatus(task.getStatus().name());
        res.setRecurrenceGroupId(task.getRecurrenceGroupId());
        if (task.getCreatedAt() != null) res.setCreatedAt(task.getCreatedAt().toString());
        if (task.getUpdatedAt() != null) res.setUpdatedAt(task.getUpdatedAt().toString());
        return res;
    }
}
