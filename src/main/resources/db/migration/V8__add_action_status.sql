ALTER TABLE lead_logs ADD COLUMN action_status VARCHAR(20) DEFAULT 'PENDING';
CREATE INDEX idx_lead_logs_action_status ON lead_logs(action_status);
