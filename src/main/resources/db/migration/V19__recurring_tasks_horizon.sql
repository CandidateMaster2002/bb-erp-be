-- Update existing recurring tasks so they are not tagged as 'TODAY' by default
UPDATE tasks 
SET time_horizon = 'RECURRING' 
WHERE recurrence_group_id IS NOT NULL;
