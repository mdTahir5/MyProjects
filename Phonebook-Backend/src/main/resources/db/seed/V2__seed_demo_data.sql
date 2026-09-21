-- =====================================================================
-- V2: seed a demo user + demo contacts for local development ONLY.
--
-- The demo login is:
--   email:    demo@phonebook.local
--   password: Password@123
--
-- The value below is a BCrypt(12) hash of exactly that password. It is
-- committed deliberately so the local demo account works out of the box;
-- NEVER reuse these credentials in a real environment.
-- =====================================================================

INSERT INTO users (name, email, password_hash, provider, email_verified, enabled)
VALUES ('Demo User',
        'demo@phonebook.local',
        '$2a$12$rcg.B4iDWdJxeJMzmTIxTeJQG51uwl0l8i5xrH3V71YMiizzPPXcO',
        'LOCAL',
        b'1',
        b'1')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO contacts (user_id, name, email, phone)
SELECT u.id, c.name, c.email, c.phone
FROM users u
         JOIN (SELECT 'Alice Johnson' AS name, 'alice@example.com' AS email, '+1 555 0100' AS phone
               UNION ALL
               SELECT 'Bob Smith', 'bob@example.com', '+1 555 0101'
               UNION ALL
               SELECT 'Carol Williams', 'carol@example.com', '+1 555 0102') c
WHERE u.email = 'demo@phonebook.local'
  AND NOT EXISTS (SELECT 1 FROM contacts x WHERE x.user_id = u.id);
