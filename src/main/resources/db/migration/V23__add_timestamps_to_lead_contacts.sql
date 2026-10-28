-- V23: Add created_at and updated_at timestamps to lead_contacts table
ALTER TABLE lead_contacts
ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Create index on updated_at for potential filtering or sorting
CREATE INDEX IF NOT EXISTS idx_lead_contacts_updated_at ON lead_contacts(updated_at);
