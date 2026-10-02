ALTER TABLE jobs
    DROP FOREIGN KEY jobs_users_id_fk;

ALTER TABLE jobs
    ADD CONSTRAINT jobs_users_id_fk
        FOREIGN KEY (recruiter_id) REFERENCES users(id)
            ON DELETE CASCADE;