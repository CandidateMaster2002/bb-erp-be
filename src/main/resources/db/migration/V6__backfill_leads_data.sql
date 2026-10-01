-- 1. Update job title and company
UPDATE leads l
SET job_title = j.job_title,
    company_name = c.name
FROM lead_jobs j
LEFT JOIN companies c ON j.company_id = c.id
WHERE l.id = j.lead_id
  AND (l.job_title IS NULL OR l.company_name IS NULL);

-- 2. Update linkedin
UPDATE leads l
SET linkedin_url = ll.linkedin_url
FROM lead_linkedin ll
WHERE l.id = ll.lead_id
  AND l.linkedin_url IS NULL;
