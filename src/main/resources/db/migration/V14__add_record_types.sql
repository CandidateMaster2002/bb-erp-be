ALTER TABLE leads ADD COLUMN record_type VARCHAR(50) NOT NULL DEFAULT 'LEAD';
CREATE INDEX idx_leads_record_type ON leads(record_type);

ALTER TABLE categories ADD COLUMN category_type VARCHAR(50) NOT NULL DEFAULT 'LEAD';
