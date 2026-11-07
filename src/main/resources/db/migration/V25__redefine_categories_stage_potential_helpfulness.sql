-- V25: Redefine categories for Stage, Business Potential, and Helpfulness
-- 1. Create 'Trash' under Stage (parent_id = 14) if not exists
INSERT INTO categories (name, parent_id)
SELECT 'Trash', 14
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Trash' AND parent_id = 14);

-- 2. Move leads currently in 'Dead' (category_id = 20) to 'Trash'
INSERT INTO lead_categories (lead_id, category_id)
SELECT lc.lead_id, c_trash.id
FROM lead_categories lc
CROSS JOIN (SELECT id FROM categories WHERE name = 'Trash' AND parent_id = 14 LIMIT 1) c_trash
WHERE lc.category_id = 20
ON CONFLICT DO NOTHING;

-- Remove the old 'Dead' mappings
DELETE FROM lead_categories WHERE category_id = 20;

-- 3. Move leads currently in 'Won' (category_id = 25) to 'Hot' (category_id = 23) in Business Potential
INSERT INTO lead_categories (lead_id, category_id)
SELECT lc.lead_id, 23
FROM lead_categories lc
WHERE lc.category_id = 25
ON CONFLICT DO NOTHING;

-- Remove the old 'Won' mappings
DELETE FROM lead_categories WHERE category_id = 25;

-- 4. Delete unused category options: 'New' (15), 'Dead' (20), 'Very Hot' (24), 'Won' (25), 'Nothing' (31)
DELETE FROM categories WHERE id IN (15, 20, 24, 25, 31);

-- 5. Update Stage category names
UPDATE categories SET name = 'Call Done' WHERE id = 18;
UPDATE categories SET name = 'Didn''t Respond' WHERE id = 17;
UPDATE categories SET name = 'I Messaged' WHERE id = 16;
UPDATE categories SET name = 'He Replied' WHERE id = 28;
UPDATE categories SET name = 'No WhatsApp No' WHERE id = 29;

-- 6. Rename 'Temperature' (19) to 'Business Potential'
UPDATE categories SET name = 'Business Potential' WHERE id = 19;

-- 7. Rename 'Relationship' (30) to 'Helpfulness'
UPDATE categories SET name = 'Helpfulness' WHERE id = 30;

