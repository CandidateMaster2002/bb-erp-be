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
    private final com.boltblazers.erp.tasks.SubActionRepository subActionRepository;

    public TaskService(TaskRepository taskRepository, com.boltblazers.erp.tasks.SubActionRepository subActionRepository) {
        this.taskRepository = taskRepository;
        this.subActionRepository = subActionRepository;
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
        if (request.getTimeHorizon() != null && !request.getTimeHorizon().isBlank()) {
            task.setTimeHorizon(request.getTimeHorizon().toUpperCase());
        } else if (task.getDeadline() != null) {
            task.setTimeHorizon("TODAY");
        } else {
            task.setTimeHorizon("SOMEDAY");
        }
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
                task.setTimeHorizon("RECURRING");
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
        if (request.getTimeHorizon() != null) task.setTimeHorizon(request.getTimeHorizon().toUpperCase());
        taskRepository.save(task);
        return toResponse(task);
    }

    public boolean isRecurringAllowed(Task task) {
        if (task.getRecurrenceGroupId() == null || task.getRecurrenceGroupId().isBlank()) {
            return true;
        }
        if (task.getDeadline() == null) {
            return false;
        }

        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        LocalDate todayIst = LocalDate.now(istZone);
        LocalDate yesterdayIst = todayIst.minusDays(1);
        LocalDate taskDateIst = task.getDeadline().atZone(istZone).toLocalDate();

        ZoneId utcZone = ZoneId.of("UTC");
        LocalDate todayUtc = LocalDate.now(utcZone);
        LocalDate yesterdayUtc = todayUtc.minusDays(1);
        LocalDate taskDateUtc = task.getDeadline().atZone(utcZone).toLocalDate();

        boolean matchIst = taskDateIst.equals(todayIst) || taskDateIst.equals(yesterdayIst);
        boolean matchUtc = taskDateUtc.equals(todayUtc) || taskDateUtc.equals(yesterdayUtc);

        return matchIst || matchUtc;
    }

    public List<TaskResponse> getTasksByHorizon(String horizon) {
        if ("TODAY".equalsIgnoreCase(horizon)) {
            List<TaskResponse> responses = new java.util.ArrayList<>();

            taskRepository.findByStatusAndTimeHorizonOrderByCreatedAtDesc(ActionStatus.PENDING, "TODAY").stream()
                    .filter(t -> t.getRecurrenceGroupId() == null || t.getRecurrenceGroupId().isBlank())
                    .map(this::toResponse)
                    .forEach(responses::add);

            taskRepository.findByStatusOrderByDeadlineAsc(ActionStatus.PENDING).stream()
                    .filter(t -> t.getRecurrenceGroupId() != null && !t.getRecurrenceGroupId().isBlank())
                    .filter(this::isRecurringAllowed)
                    .map(this::toResponse)
                    .forEach(responses::add);

            return responses;
        } else {
            return taskRepository.findByStatusAndTimeHorizonOrderByCreatedAtDesc(ActionStatus.PENDING, horizon.toUpperCase()).stream()
                    .filter(t -> t.getRecurrenceGroupId() == null || t.getRecurrenceGroupId().isBlank())
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }
    }

    public TaskResponse updateTaskHorizon(Long taskId, String horizon, String deadlineStr) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (horizon != null && !horizon.isBlank()) {
            task.setTimeHorizon(horizon.toUpperCase());
        }
        if (deadlineStr != null) {
            if (deadlineStr.isBlank()) {
                task.setDeadline(null);
            } else {
                task.setDeadline(parseDate(deadlineStr));
            }
        }
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
                .stream()
                .filter(this::isRecurringAllowed)
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> getOverdueAndTodayActions() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        Instant end = today.plusDays(1).atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant();
        return taskRepository.findByStatusAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus.PENDING, end).stream()
                .filter(this::isRecurringAllowed)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getActionsBetween(LocalDate from, LocalDate to) {
        Instant start = from.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = to.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        return taskRepository.findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus.PENDING, start, end)
                .stream()
                .filter(this::isRecurringAllowed)
                .map(this::toResponse).collect(Collectors.toList());
    }
    
    public List<TaskResponse> getByStatus(ActionStatus status) {
        return taskRepository.findByStatusOrderByDeadlineAsc(status).stream()
                .filter(this::isRecurringAllowed)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTask(Long taskId) {
        return toResponse(taskRepository.findById(taskId).orElseThrow());
    }

    public com.boltblazers.erp.tasks.dto.SubActionResponse addSubAction(Long taskId, com.boltblazers.erp.tasks.dto.SubActionRequest req) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        com.boltblazers.erp.tasks.SubAction sa = new com.boltblazers.erp.tasks.SubAction();
        sa.setTask(task);
        sa.setTitle(req.getTitle());
        sa.setDescription(req.getDescription());
        sa.setDueDate(parseDate(req.getDueDate()));
        sa.setStatus(com.boltblazers.erp.tasks.SubActionStatus.PENDING);
        return toSubActionResponse(subActionRepository.save(sa));
    }

    public com.boltblazers.erp.tasks.dto.SubActionResponse updateSubAction(Long saId, com.boltblazers.erp.tasks.dto.SubActionRequest req) {
        com.boltblazers.erp.tasks.SubAction sa = subActionRepository.findById(saId).orElseThrow();
        if (req.getTitle() != null) sa.setTitle(req.getTitle());
        if (req.getDescription() != null) sa.setDescription(req.getDescription());
        if (req.getDueDate() != null) sa.setDueDate(parseDate(req.getDueDate()));
        return toSubActionResponse(subActionRepository.save(sa));
    }

    public com.boltblazers.erp.tasks.dto.SubActionResponse changeSubActionStatus(Long saId, String statusStr) {
        com.boltblazers.erp.tasks.SubAction sa = subActionRepository.findById(saId).orElseThrow();
        sa.setStatus(com.boltblazers.erp.tasks.SubActionStatus.valueOf(statusStr.toUpperCase()));
        return toSubActionResponse(subActionRepository.save(sa));
    }

    public void deleteSubAction(Long saId) {
        subActionRepository.deleteById(saId);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        if (task.getDeadline() != null) res.setDeadline(task.getDeadline().toString());
        res.setStatus(task.getStatus().name());
        res.setRecurrenceGroupId(task.getRecurrenceGroupId());

        if (task.getRecurrenceGroupId() != null && !task.getRecurrenceGroupId().isBlank()) {
            if (task.getDeadline() != null) {
                ZoneId istZone = ZoneId.of("Asia/Kolkata");
                LocalDate taskDate = task.getDeadline().atZone(istZone).toLocalDate();
                LocalDate today = LocalDate.now(istZone);
                if (taskDate.equals(today)) {
                    res.setTimeHorizon("Today");
                } else if (taskDate.equals(today.minusDays(1))) {
                    res.setTimeHorizon("Yesterday");
                } else {
                    res.setTimeHorizon("Recurring");
                }
            } else {
                res.setTimeHorizon("Recurring");
            }
        } else {
            res.setTimeHorizon(task.getTimeHorizon());
        }

        if (task.getCreatedAt() != null) res.setCreatedAt(task.getCreatedAt().toString());
        if (task.getUpdatedAt() != null) res.setUpdatedAt(task.getUpdatedAt().toString());

        List<com.boltblazers.erp.tasks.dto.SubActionResponse> subs = subActionRepository.findByTask(task)
            .stream().map(this::toSubActionResponse).collect(Collectors.toList());
        res.setSubactions(subs);

        return res;
    }

    private com.boltblazers.erp.tasks.dto.SubActionResponse toSubActionResponse(com.boltblazers.erp.tasks.SubAction sa) {
        com.boltblazers.erp.tasks.dto.SubActionResponse r = new com.boltblazers.erp.tasks.dto.SubActionResponse();
        r.setId(sa.getId());
        r.setTaskId(sa.getTask().getId());
        r.setTitle(sa.getTitle());
        r.setDescription(sa.getDescription());
        if (sa.getDueDate() != null) r.setDueDate(sa.getDueDate().toString());
        r.setStatus(sa.getStatus().name());
        if (sa.getCreatedAt() != null) r.setCreatedAt(sa.getCreatedAt().toString());
        if (sa.getUpdatedAt() != null) r.setUpdatedAt(sa.getUpdatedAt().toString());
        return r;
    }
}
