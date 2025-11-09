-- 2. Create application user
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'username') THEN
        CREATE USER username WITH PASSWORD 'test';
    END IF;
END
$$;

-- 3. Grant database-level privileges
GRANT ALL PRIVILEGES ON DATABASE whatsapp_testing TO username;

-- 4. Connect to the database
-- Note: You may need to reconnect to the whatsapp_testing database for remaining commands

-- 5. Transfer ownership
ALTER DATABASE whatsapp_testing OWNER TO username;

-- 6. Grant schema privileges
GRANT ALL ON SCHEMA public TO username;

-- 7. Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public 
GRANT ALL ON TABLES TO username;

ALTER DEFAULT PRIVILEGES IN SCHEMA public 
GRANT ALL ON SEQUENCES TO username;

ALTER DEFAULT PRIVILEGES IN SCHEMA public 
GRANT ALL ON FUNCTIONS TO username;

-- 8. Verify setup
SELECT 
    'Database created: whatsapp_testing' as status,
    current_database() as database,
    session_user as current_user;

-- 9. Test table creation (as username)
SET ROLE username;

CREATE TABLE IF NOT EXISTS test_connection (
    id SERIAL PRIMARY KEY,
    test_field VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO test_connection (test_field) VALUES ('Connection test successful');

SELECT * FROM test_connection;

-- 10. Clean up test table
DROP TABLE test_connection;

-- Reset role
RESET ROLE;

-- Done!
SELECT 'Setup completed successfully!' as message;