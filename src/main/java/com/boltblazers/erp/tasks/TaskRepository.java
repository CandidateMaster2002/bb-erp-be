package com.boltblazers.erp.tasks;

import com.boltblazers.erp.leads.ActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusOrderByDeadlineAsc(ActionStatus status);

    List<Task> findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus status, Instant start, Instant end);

    List<Task> findByStatusAndDeadlineLessThanOrderByDeadlineAsc(ActionStatus status, Instant end);
}
