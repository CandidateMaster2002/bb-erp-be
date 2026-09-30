-- 12. imports (referenced by leads)
CREATE TABLE imports (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    imported_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_rows INT NOT NULL DEFAULT 0,
    notes TEXT
);

-- 1. stages
CREATE TABLE stages (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO stages (name, sort_order, is_active) VALUES
    ('New', 10, true),
    ('Contacted', 20, true),
    ('Interested', 30, true),
    ('Proposal Sent', 40, true),
    ('Negotiation', 50, true),
    ('Won', 60, true),
    ('Lost', 70, true),
    ('On Hold', 80, true);

-- 2. categories
CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES categories(id) ON DELETE CASCADE
);

CREATE INDEX idx_categories_parent_id ON categories(parent_id);

-- Seed categories
WITH ism AS (INSERT INTO categories (name) VALUES ('ISM Alumni') RETURNING id),
     ism_f AS (INSERT INTO categories (name, parent_id) SELECT 'Founders', id FROM ism),
     iit AS (INSERT INTO categories (name) VALUES ('IIT Roorkee') RETURNING id),
     iit_f AS (INSERT INTO categories (name, parent_id) SELECT 'Founders', id FROM iit),
     pi AS (INSERT INTO categories (name) VALUES ('Property Inspection') RETURNING id)
INSERT INTO categories (name, parent_id) SELECT 'Founders', id FROM pi;

-- 3. leads
CREATE TABLE leads (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    headline TEXT,
    summary TEXT,
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    location TEXT,
    profile_picture_url TEXT,
    stage_id BIGINT REFERENCES stages(id) ON DELETE SET NULL,
    priority VARCHAR(50), -- HOT/WARM/COLD
    source VARCHAR(255),
    import_id BIGINT REFERENCES imports(id) ON DELETE SET NULL,
    dedup_key_used VARCHAR(255),
    remark TEXT,
    next_followup_at TIMESTAMPTZ,
    last_contacted_at TIMESTAMPTZ,
    lost_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_leads_stage_id ON leads(stage_id);
CREATE INDEX idx_leads_import_id ON leads(import_id);

-- 4. lead_contacts
CREATE TABLE lead_contacts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- MOBILE, WHATSAPP, PERSONAL_EMAIL, WORK_EMAIL, ASSUMED_MOBILE
    value VARCHAR(255) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_lead_contacts_lead_id ON lead_contacts(lead_id);
CREATE INDEX idx_lead_contacts_value ON lead_contacts(value);

-- 5. lead_linkedin
CREATE TABLE lead_linkedin (
    lead_id BIGINT PRIMARY KEY REFERENCES leads(id) ON DELETE CASCADE,
    linkedin_url TEXT,
    member_urn VARCHAR(255),
    public_identifier VARCHAR(255),
    followers_count INT,
    premium BOOLEAN,
    personal_website TEXT
);

-- 6. companies
CREATE TABLE companies (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    linkedin_id VARCHAR(255),
    linkedin_url TEXT,
    website TEXT,
    industry VARCHAR(255),
    employees VARCHAR(100),
    founded_year INT,
    location VARCHAR(255),
    specialities TEXT,
    description TEXT
);

-- 7. lead_jobs
CREATE TABLE lead_jobs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    company_id BIGINT REFERENCES companies(id) ON DELETE CASCADE,
    job_title VARCHAR(255),
    job_description TEXT,
    started_on VARCHAR(100),
    is_current BOOLEAN DEFAULT FALSE,
    recently_hired BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_lead_jobs_lead_id ON lead_jobs(lead_id);
CREATE INDEX idx_lead_jobs_company_id ON lead_jobs(company_id);

-- 8. lead_education
CREATE TABLE lead_education (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    college VARCHAR(255),
    degree VARCHAR(255),
    branch VARCHAR(255),
    batch_start VARCHAR(100),
    batch_end VARCHAR(100)
);

CREATE INDEX idx_lead_education_lead_id ON lead_education(lead_id);

-- 9. lead_categories
CREATE TABLE lead_categories (
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (lead_id, category_id)
);

CREATE INDEX idx_lead_categories_category_id ON lead_categories(category_id);

-- 10. interactions
CREATE TABLE interactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- CALL, WHATSAPP, MEETING, EMAIL, NOTE
    outcome VARCHAR(50), -- PICKED_UP, NOT_REACHABLE, BUSY, CALL_LATER, NOT_INTERESTED, NONE
    summary TEXT,
    occurred_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_interactions_lead_id ON interactions(lead_id);

-- 11. followups
CREATE TABLE followups (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    due_at TIMESTAMPTZ NOT NULL,
    note TEXT,
    status VARCHAR(50) NOT NULL, -- PENDING, DONE, SNOOZED
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_followups_lead_id ON followups(lead_id);

-- 13. commitments
CREATE TABLE commitments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    item TEXT NOT NULL,
    due_date DATE,
    status VARCHAR(50) NOT NULL, -- PENDING, SENT
    file_or_link TEXT,
    sent_at TIMESTAMPTZ
);

CREATE INDEX idx_commitments_lead_id ON commitments(lead_id);

-- 14. tags and lead_tags
CREATE TABLE tags (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE lead_tags (
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (lead_id, tag_id)
);

CREATE INDEX idx_lead_tags_tag_id ON lead_tags(tag_id);
