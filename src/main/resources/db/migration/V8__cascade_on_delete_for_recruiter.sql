-- Fix the Freelancer relationship on applications
ALTER TABLE applications
    DROP FOREIGN KEY applications_users_id_fk;

ALTER TABLE applications
    ADD CONSTRAINT applications_users_id_fk
        FOREIGN KEY (freelancer_id) REFERENCES users(id)
            ON DELETE CASCADE;