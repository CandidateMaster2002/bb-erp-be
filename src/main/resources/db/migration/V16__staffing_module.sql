CREATE TABLE demand_sources (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE requirements (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    demand_source_id BIGINT NOT NULL REFERENCES demand_sources(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    jd_text TEXT,
    ctc VARCHAR(100),
    notice_period VARCHAR(100),
    experience_range VARCHAR(100),
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE supply_sources (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE candidates (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    supply_source_id BIGINT REFERENCES supply_sources(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    mobile_no VARCHAR(50),
    resume_link TEXT,
    current_ctc VARCHAR(100),
    expected_ctc VARCHAR(100),
    notice_period VARCHAR(100),
    gap VARCHAR(100),
    college_tier VARCHAR(100),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE submissions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requirement_id BIGINT NOT NULL REFERENCES requirements(id) ON DELETE CASCADE,
    candidate_id BIGINT NOT NULL REFERENCES candidates(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL DEFAULT 'SCREENING',
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(requirement_id, candidate_id)
);
