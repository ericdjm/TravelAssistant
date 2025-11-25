-- ============================================================================
-- CPS731 Travel Assistant - Database Schema
-- Team 20: Hussein Saab, Eric Mergelas, Omar Ahmed
-- Phase 3: System Implementation
-- ============================================================================
-- Database: travel_assistant_db
-- Tables: profiles, sessions, conversation_history, events
-- Version: 1.0
-- Last Updated: November 20, 2024
-- ============================================================================

-- Use the travel assistant database
USE travel_assistant_db;

-- Drop existing tables (if re-running schema)
DROP TABLE IF EXISTS conversation_history;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS profiles;

-- ============================================================================
-- Table 1: profiles
-- Purpose: Store user preferences for personalized recommendations
-- Traceability: FR-6, UC-4, Class: Profile.java
-- ============================================================================
CREATE TABLE profiles (
    user_id VARCHAR(50) PRIMARY KEY COMMENT 'Unique user identifier (maps to UserID.java)',
    interests JSON COMMENT 'Array of interest categories, e.g., ["restaurants", "museums", "parks"]',
    budget VARCHAR(20) COMMENT 'Budget preference: "low", "medium", or "high"',
    radius INT COMMENT 'Search radius in meters (e.g., 2000 for 2km)',
    transport_mode VARCHAR(20) COMMENT 'Preferred transport: "walking", "transit", or "driving"',
    accessibility_needs BOOLEAN DEFAULT FALSE COMMENT 'Accessibility requirements (NFR-6)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation timestamp',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',

    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User profiles with travel preferences (FR-6)';

-- ============================================================================
-- Table 2: sessions
-- Purpose: Track user sessions for analytics and context
-- Traceability: FR-5, FR-17, NFR-3, UC-1, UC-4, Class: Session.java
-- ============================================================================
CREATE TABLE sessions (
    session_id VARCHAR(100) PRIMARY KEY COMMENT 'Unique session identifier (maps to SessionID.java)',
    user_id VARCHAR(50) COMMENT 'User ID (nullable for guest sessions)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Session start time',
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last activity timestamp',
    request_count INT DEFAULT 0 COMMENT 'Number of requests in this session (for rate limiting NFR-3)',

    FOREIGN KEY (user_id) REFERENCES profiles(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_last_active (last_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User sessions for tracking and context (FR-5, FR-17)';

-- ============================================================================
-- Table 3: conversation_history
-- Purpose: Store chat messages for conversational context
-- Traceability: FR-4, FR-17, UC-1, UC-2, Class: ConversationEngine.java
-- ============================================================================
CREATE TABLE conversation_history (
    message_id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique message identifier',
    session_id VARCHAR(100) NOT NULL COMMENT 'Session this message belongs to',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Message timestamp',
    role ENUM('user', 'assistant') NOT NULL COMMENT 'Message sender: user or assistant (chatbot)',
    message_text TEXT NOT NULL COMMENT 'The actual message content',
    preferences_snapshot JSON COMMENT 'Preferences state at message time (for context)',

    FOREIGN KEY (session_id) REFERENCES sessions(session_id) ON DELETE CASCADE,
    INDEX idx_session_timestamp (session_id, timestamp) COMMENT 'Fast retrieval of conversation by session'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Conversation history for context maintenance (FR-4)';

-- ============================================================================
-- Table 4: events
-- Purpose: Log user interactions for analytics and rate limiting
-- Traceability: FR-20, NFR-3, UC-6, Class: AnalyticsLogger.java
-- ============================================================================
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique event identifier',
    event_type VARCHAR(50) NOT NULL COMMENT 'Event category: search, select_card, view_itinerary, rate_limit, etc.',
    user_id VARCHAR(50) COMMENT 'User who triggered event (nullable for guests)',
    session_id VARCHAR(100) COMMENT 'Session where event occurred (nullable)',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Event timestamp',
    details JSON COMMENT 'Event-specific data (flexible schema)',

    FOREIGN KEY (user_id) REFERENCES profiles(user_id) ON DELETE SET NULL,
    FOREIGN KEY (session_id) REFERENCES sessions(session_id) ON DELETE SET NULL,
    INDEX idx_user_timestamp (user_id, timestamp) COMMENT 'Fast rate limit queries',
    INDEX idx_event_type (event_type) COMMENT 'Fast analytics queries by type',
    INDEX idx_timestamp (timestamp) COMMENT 'Fast time-range queries'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Analytics and event logging (FR-20, NFR-3)';

-- ============================================================================
-- Insert Sample Data (for testing and TA demo)
-- ============================================================================

-- Sample Profile 1: Hussein (authenticated user)
INSERT INTO profiles (user_id, interests, budget, radius, transport_mode, accessibility_needs)
VALUES (
    'user_hussein',
    '["restaurants", "museums", "parks"]',
    'medium',
    2000,
    'walking',
    false
);

-- Sample Profile 2: Eric (authenticated user with different preferences)
INSERT INTO profiles (user_id, interests, budget, radius, transport_mode, accessibility_needs)
VALUES (
    'user_eric',
    '["cafes", "art galleries", "bookstores"]',
    'high',
    5000,
    'transit',
    true
);

-- Sample Session 1: Hussein's session
INSERT INTO sessions (session_id, user_id, request_count)
VALUES (
    'session_001',
    'user_hussein',
    5
);

-- Sample Session 2: Guest session (no user_id)
INSERT INTO sessions (session_id, user_id, request_count)
VALUES (
    'session_guest_001',
    NULL,
    2
);

-- Sample Conversation History
INSERT INTO conversation_history (session_id, role, message_text, preferences_snapshot)
VALUES
    ('session_001', 'user', 'Show me restaurants near me', '{"budget": "medium", "radius": 2000}'),
    ('session_001', 'assistant', 'I found 5 restaurants within 2km. Here are the top 3...', NULL),
    ('session_001', 'user', 'Can you show me museums instead?', '{"budget": "medium", "radius": 2000}'),
    ('session_001', 'assistant', 'Sure! Here are 3 museums nearby...', NULL);

-- Sample Events
INSERT INTO events (event_type, user_id, session_id, details)
VALUES
    ('search', 'user_hussein', 'session_001', '{"query": "restaurants", "result_count": 5}'),
    ('select_card', 'user_hussein', 'session_001', '{"poi_id": "poi_123", "name": "Pasta Palace"}'),
    ('view_itinerary', 'user_hussein', 'session_001', '{"poi_id": "poi_123", "steps": 3}'),
    ('search', NULL, 'session_guest_001', '{"query": "cafes", "result_count": 3}'),
    ('rate_limit', 'user_hussein', 'session_001', '{"limit": 30, "current": 31}');

-- ============================================================================
-- Verify Schema Creation
-- ============================================================================

-- Show all tables
SHOW TABLES;

-- Show table structures
DESCRIBE profiles;
DESCRIBE sessions;
DESCRIBE conversation_history;
DESCRIBE events;

-- Show sample data counts
SELECT 'profiles' as table_name, COUNT(*) as row_count FROM profiles
UNION ALL
SELECT 'sessions', COUNT(*) FROM sessions
UNION ALL
SELECT 'conversation_history', COUNT(*) FROM conversation_history
UNION ALL
SELECT 'events', COUNT(*) FROM events;

-- ============================================================================
-- Test Queries (to verify schema works)
-- ============================================================================

-- Query 1: Get user profile with session count
SELECT
    p.user_id,
    p.interests,
    p.budget,
    COUNT(s.session_id) as total_sessions
FROM profiles p
LEFT JOIN sessions s ON p.user_id = s.user_id
GROUP BY p.user_id;

-- Query 2: Get conversation history for a session
SELECT
    message_id,
    role,
    message_text,
    timestamp
FROM conversation_history
WHERE session_id = 'session_001'
ORDER BY timestamp ASC;

-- Query 3: Rate limit check (requests in last minute)
SELECT
    user_id,
    COUNT(*) as request_count_last_minute
FROM events
WHERE user_id = 'user_hussein'
  AND event_type = 'search'
  AND timestamp > DATE_SUB(NOW(), INTERVAL 1 MINUTE)
GROUP BY user_id;

-- Query 4: Analytics report (event counts by type)
SELECT
    event_type,
    COUNT(*) as event_count
FROM events
GROUP BY event_type
ORDER BY event_count DESC;

-- ============================================================================
-- Schema Information
-- ============================================================================

SELECT
    TABLE_NAME,
    TABLE_ROWS,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'travel_assistant_db'
ORDER BY TABLE_NAME;

-- ============================================================================
-- End of Schema
-- ============================================================================

COMMIT;

SELECT '✅ Schema created successfully!' AS status;
SELECT '📊 Sample data inserted for testing' AS note;
SELECT '🚀 Ready for Java integration' AS next_step;
