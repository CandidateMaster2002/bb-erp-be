ALTER TABLE lead_logs ALTER COLUMN next_action_date TYPE TIMESTAMPTZ USING next_action_date::TIMESTAMPTZ;
