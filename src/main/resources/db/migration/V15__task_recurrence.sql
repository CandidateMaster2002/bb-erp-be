ALTER TABLE tasks ADD COLUMN recurrence_group_id VARCHAR(255);
CREATE INDEX idx_tasks_recurrence_group_id ON tasks(recurrence_group_id);
