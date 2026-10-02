UPDATE applications
SET status = UPPER(status)
WHERE status = 'pending';