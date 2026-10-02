-- Simple activity log per lead
CREATE TABLE lead_logs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    comment TEXT,
    next_action TEXT,
    next_action_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_lead_logs_lead_id ON lead_logs(lead_id);
CREATE INDEX idx_lead_logs_next_action_date ON lead_logs(next_action_date);
