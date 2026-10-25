-- 1. Insert existing category names into the tags table
INSERT INTO tags (name)
SELECT DISTINCT TRIM(c.name)
FROM categories c
WHERE TRIM(c.name) <> ''
  AND NOT EXISTS (
      SELECT 1 FROM tags t WHERE LOWER(TRIM(t.name)) = LOWER(TRIM(c.name))
  );

-- 2. Backfill lead_tags from lead_categories so existing leads keep their tags
INSERT INTO lead_tags (lead_id, tag_id)
SELECT DISTINCT lc.lead_id, t.id
FROM lead_categories lc
JOIN categories c ON lc.category_id = c.id
JOIN tags t ON LOWER(TRIM(c.name)) = LOWER(TRIM(t.name))
ON CONFLICT (lead_id, tag_id) DO NOTHING;
