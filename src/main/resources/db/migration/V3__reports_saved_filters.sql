CREATE TABLE saved_filters (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL, -- references auth users conceptually
    name VARCHAR(255) NOT NULL,
    filter_json TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_saved_filters_user_id ON saved_filters(user_id);

ALTER TABLE followups ADD COLUMN recurrence_days INT;
ALTER TABLE followups ADD COLUMN acknowledged_at TIMESTAMPTZ;
