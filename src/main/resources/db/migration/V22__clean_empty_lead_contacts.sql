-- Delete any empty or invalid phone contact records (less than 5 digits)
DELETE FROM lead_contacts 
WHERE type IN ('MOBILE', 'ASSUMED_MOBILE', 'WHATSAPP')
  AND (
      value IS NULL 
      OR TRIM(value) = ''
      OR LENGTH(REGEXP_REPLACE(value, '[^0-9]', '', 'g')) < 5
  );
