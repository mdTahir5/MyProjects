# =====================================================================
# MySQL bootstrap for the Phonebook application (local development).
#
# Run once as a MySQL administrator:
#   mysql -u root -p < scripts/setup-mysql.sql
#
# Creates the database, an application user with the minimum privileges it
# needs, and nothing else. No Docker, no containers.
# =====================================================================

-- ---------------------------------------------------------------------
-- Database
-- ---------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS defaultdb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Application user (change the password before using this anywhere real,
-- then set the same value in backend/.env as DB_PASSWORD)
-- ---------------------------------------------------------------------
CREATE USER IF NOT EXISTS 'avnadmin'@'mysql-phonebook-01-imperialtslearn-1cbd.g.aivencloud.com'
    IDENTIFIED BY 'AVNS_LVyRfkDtTYChsqi7wxB';

-- Flyway owns the schema, so the application needs DDL rights.
-- These grants are limited to this one database.
GRANT SELECT, INSERT, UPDATE, DELETE,
      CREATE, ALTER, DROP, INDEX, REFERENCES
    ON defaultdb.*
    TO 'avnadmin'@'mysql-phonebook-01-imperialtslearn-1cbd.g.aivencloud.com';

FLUSH PRIVILEGES;

-- ---------------------------------------------------------------------
-- Verify
-- ---------------------------------------------------------------------
SELECT
    SCHEMA_NAME                 AS `database`,
    DEFAULT_CHARACTER_SET_NAME  AS charset,
    DEFAULT_COLLATION_NAME      AS collation
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'defaultdb';

SELECT GRANTEE, PRIVILEGE_TYPE
FROM information_schema.USER_PRIVILEGES
WHERE GRANTEE = "'avnadmin'@'mysql-phonebook-01-imperialtslearn-1cbd.g.aivencloud.com'";
