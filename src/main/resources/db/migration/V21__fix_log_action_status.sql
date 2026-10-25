-- Set action_status to NULL for notes/logs that have no next_action and no next_action_date
UPDATE lead_logs 
SET action_status = NULL 
WHERE (next_action IS NULL OR TRIM(next_action) = '') 
  AND next_action_date IS NULL;
