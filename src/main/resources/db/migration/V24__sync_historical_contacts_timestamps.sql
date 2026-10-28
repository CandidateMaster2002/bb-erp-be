-- V24: Sync historical contact timestamps with their parent lead's created_at
-- This ensures that only contacts actually modified today reflect today's date,
-- while historical contacts created before today reflect their lead's creation date.
UPDATE lead_contacts lc
SET created_at = l.created_at,
    updated_at = l.created_at
FROM leads l
WHERE lc.lead_id = l.id
  AND lc.lead_id NOT IN (
    255, 260, 262, 279, 289, 301, 302, 307, 325, 348, 358, 367, 368, 373, 374, 384, 393, 400, 410, 411, 
    418, 421, 432, 437, 453, 455, 459, 468, 469, 495, 500, 520, 528, 533, 534, 544, 546, 547, 553, 555, 
    557, 561, 562, 563, 565, 566, 569, 573, 585, 586, 588, 595, 603, 608, 610, 658, 662, 670, 677, 678, 679
  );
