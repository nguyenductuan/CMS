-- MySQL migration: add login credentials and account role to existing user table.
ALTER TABLE user
    ADD COLUMN username VARCHAR(50) NULL,
    ADD COLUMN password VARCHAR(100) NULL,
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'SELLER';

-- Existing rows must be assigned unique usernames before making username mandatory.
-- Existing users receive a deterministic username based on their id.
UPDATE user
SET username = CONCAT('user_', id)
WHERE username IS NULL OR username = '';

ALTER TABLE user
    MODIFY COLUMN username VARCHAR(50) NOT NULL,
    ADD UNIQUE KEY uk_user_username (username);
