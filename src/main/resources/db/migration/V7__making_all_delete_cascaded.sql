-- 1. Cascade from jobs -> applications
ALTER TABLE applications
    DROP FOREIGN KEY applications_jobs_id_fk;

ALTER TABLE applications
    ADD CONSTRAINT applications_jobs_id_fk
        FOREIGN KEY (job_id) REFERENCES jobs(id)
            ON DELETE CASCADE;

-- 2. Cascade from applications -> accepted_jobs (if applicable)
ALTER TABLE accepted_jobs
    DROP FOREIGN KEY accepted_jobs_applications_id_fk;

ALTER TABLE accepted_jobs
    ADD CONSTRAINT accepted_jobs_applications_id_fk
    FOREIGN KEY (application_id) REFERENCES applications(id)
        ON DELETE CASCADE;

-- 3. Cascade from users -> jobs
ALTER TABLE jobs
    DROP FOREIGN KEY jobs_users_id_fk;

ALTER TABLE jobs
    ADD CONSTRAINT jobs_users_id_fk
        FOREIGN KEY (recruiter_id) REFERENCES users(id)
            ON DELETE CASCADE;