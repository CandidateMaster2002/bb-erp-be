ALTER TABLE tasks ADD COLUMN time_horizon VARCHAR(50) DEFAULT 'TODAY';
CREATE INDEX idx_tasks_time_horizon ON tasks(time_horizon);

CREATE TABLE vault_items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL DEFAULT 'IDEA',
    link TEXT,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_vault_items_category ON vault_items(category);
