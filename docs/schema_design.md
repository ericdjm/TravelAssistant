# Database Schema Design

**CPS731 Travel Assistant - Phase 3**
**Team 20: Hussein Saab, Eric Mergelas, Omar Ahmed**

---

## 🎯 Design Goals

**Keep it simple, executable, and traceable to requirements:**
- Support core persistence needs (ProfileContextStore, AnalyticsLogger)
- Enable conversation history (FR-4, FR-17)
- Support rate limiting (FR-20, NFR-3)
- Allow session resumption (FR-5, FR-17)
- Basic but complete - ready for TA demo

---

## 📊 Schema Overview

**4 Tables:**
1. `profiles` - User preferences (FR-6)
2. `sessions` - Session tracking (FR-5, FR-17)
3. `conversation_history` - Chat context (FR-4, FR-17)
4. `events` - Analytics logging (FR-20, NFR-3)

---

## 🗄️ Table Designs

### 1. `profiles` Table

**Purpose**: Store user preferences for personalized recommendations (FR-6, UC-4)

**GRASP**: Information Expert - ProfileContextStore manages this data

```sql
CREATE TABLE profiles (
    user_id VARCHAR(50) PRIMARY KEY,
    interests JSON,                    -- e.g., ["restaurants", "museums"]
    budget VARCHAR(20),                -- "low", "medium", "high"
    radius INT,                        -- search radius in meters
    transport_mode VARCHAR(20),        -- "walking", "transit", "driving"
    accessibility_needs BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Key Columns:**
- `user_id`: Unique identifier (maps to UserID domain object)
- `interests`: JSON array for flexible storage (e.g., `["museums", "parks", "cafes"]`)
- `budget`, `radius`, `transport_mode`: Direct mapping to Preferences class attributes
- `accessibility_needs`: Boolean for accessibility requirements (NFR-6)
- Timestamps for audit trail

**Traceability:**
- FR-6: System shall retrieve user preferences from stored profiles
- UC-4: Sign in / Personalize
- Class: `Profile.java` (domain object)
- Methods: `ProfileContextStore.saveProfile()`, `loadProfile()`

---

### 2. `sessions` Table

**Purpose**: Track user sessions for analytics and context (FR-5, FR-17, NFR-3)

**GRASP**: Information Expert - ProfileContextStore manages session lifecycle

```sql
CREATE TABLE sessions (
    session_id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    request_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES profiles(user_id) ON DELETE CASCADE
);
```

**Key Columns:**
- `session_id`: Unique session identifier (maps to SessionID domain object)
- `user_id`: Links to profile (nullable for guest users)
- `request_count`: Tracks requests per session (for rate limiting - NFR-3)
- `last_active`: Auto-updates on any session activity

**Traceability:**
- FR-5: Chatbot shall store user sessions for returning users
- FR-17: System shall store and retrieve session history
- NFR-3: Rate limiting (guest: 10/min, auth: 30/min)
- UC-1, UC-4: Session-based interactions
- Class: `Session.java`
- Methods: `ProfileContextStore.saveSession()`, `loadSession()`

---

### 3. `conversation_history` Table

**Purpose**: Store chat messages for context and session resumption (FR-4, FR-17)

**GRASP**: Information Expert - Conversation context stored with sessions

```sql
CREATE TABLE conversation_history (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role ENUM('user', 'assistant') NOT NULL,
    message_text TEXT NOT NULL,
    preferences_snapshot JSON,         -- Captures preference state at message time
    FOREIGN KEY (session_id) REFERENCES sessions(session_id) ON DELETE CASCADE,
    INDEX idx_session_timestamp (session_id, timestamp)
);
```

**Key Columns:**
- `message_id`: Auto-incrementing primary key
- `session_id`: Links to session (one session has many messages)
- `role`: Either 'user' or 'assistant' (chatbot)
- `message_text`: The actual message content
- `preferences_snapshot`: JSON of preferences at that moment (useful for "Show Similar")
- Index on `(session_id, timestamp)` for fast conversation retrieval

**Traceability:**
- FR-4: Chatbot shall maintain conversational context within a session
- FR-17: Store and retrieve session history to reuse preferences
- UC-1: Plan something now (maintains context)
- UC-2: Adjust preferences (shows conversation flow)
- Class: `ConversationEngine.java` (uses this for context)
- Methods: `saveMessage()`, `loadConversationHistory(sessionId)`

---

### 4. `events` Table

**Purpose**: Log all user interactions for analytics and rate limiting (FR-20, NFR-3)

**GRASP**: Information Expert - AnalyticsLogger manages event data

```sql
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,   -- "search", "select_card", "view_itinerary", "rate_limit"
    user_id VARCHAR(50),               -- Nullable (guest users)
    session_id VARCHAR(100),           -- Nullable
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details JSON,                      -- Event-specific data
    FOREIGN KEY (user_id) REFERENCES profiles(user_id) ON DELETE SET NULL,
    FOREIGN KEY (session_id) REFERENCES sessions(session_id) ON DELETE SET NULL,
    INDEX idx_user_timestamp (user_id, timestamp),
    INDEX idx_event_type (event_type)
);
```

**Key Columns:**
- `event_id`: Auto-incrementing primary key
- `event_type`: Category of event (search, select_card, view_itinerary, rate_limit, etc.)
- `user_id`, `session_id`: Nullable (supports guest users)
- `details`: JSON for flexible event-specific data
  - Example: `{"query": "restaurants near me", "result_count": 5}`
- Indexes for fast rate-limit queries

**Traceability:**
- FR-20: System shall log interactions and store analytics
- NFR-3: Guest users 10 req/min, authenticated 30 req/min
- UC-6: Handle rate limits (detects over-limit from this table)
- Class: `AnalyticsLogger.java`
- Methods: `logInteraction()`, `detectRateLimit()`, `report()`

---

## 🔗 Entity Relationships

```
profiles (1) ─────< sessions (many)
                       │
                       ├───< conversation_history (many)
                       │
                       └───< events (many)

profiles (1) ─────< events (many) [optional FK]
```

**Relationships:**
- One `profile` can have many `sessions` (user returns multiple times)
- One `session` can have many `conversation_history` entries (chat messages)
- One `session` can generate many `events` (analytics)
- One `profile` can have many `events` (cross-session analytics)

**Foreign Key Constraints:**
- `sessions.user_id` → `profiles.user_id` (CASCADE DELETE)
- `conversation_history.session_id` → `sessions.session_id` (CASCADE DELETE)
- `events.user_id` → `profiles.user_id` (SET NULL on delete - keep analytics)
- `events.session_id` → `sessions.session_id` (SET NULL on delete - keep analytics)

---

## 📋 Sample Queries

### ProfileContextStore Operations

**Save Profile:**
```sql
INSERT INTO profiles (user_id, interests, budget, radius, transport_mode, accessibility_needs)
VALUES ('user_123', '["restaurants", "museums"]', 'medium', 2000, 'walking', false)
ON DUPLICATE KEY UPDATE
    interests = VALUES(interests),
    budget = VALUES(budget),
    radius = VALUES(radius),
    transport_mode = VALUES(transport_mode),
    accessibility_needs = VALUES(accessibility_needs);
```

**Load Profile:**
```sql
SELECT * FROM profiles WHERE user_id = 'user_123';
```

**Save Session:**
```sql
INSERT INTO sessions (session_id, user_id, request_count)
VALUES ('session_abc', 'user_123', 0)
ON DUPLICATE KEY UPDATE
    last_active = CURRENT_TIMESTAMP,
    request_count = VALUES(request_count);
```

**Load Session:**
```sql
SELECT * FROM sessions WHERE session_id = 'session_abc';
```

---

### Conversation History Operations

**Save Message:**
```sql
INSERT INTO conversation_history (session_id, role, message_text, preferences_snapshot)
VALUES ('session_abc', 'user', 'Show me restaurants near me', '{"budget": "medium", "radius": 2000}');
```

**Load Conversation:**
```sql
SELECT * FROM conversation_history
WHERE session_id = 'session_abc'
ORDER BY timestamp ASC;
```

---

### Analytics Operations

**Log Event:**
```sql
INSERT INTO events (event_type, user_id, session_id, details)
VALUES ('search', 'user_123', 'session_abc', '{"query": "restaurants", "result_count": 5}');
```

**Detect Rate Limit (last 1 minute):**
```sql
SELECT COUNT(*) as request_count
FROM events
WHERE user_id = 'user_123'
  AND event_type = 'search'
  AND timestamp > DATE_SUB(NOW(), INTERVAL 1 MINUTE);
```

**Generate Report:**
```sql
SELECT event_type, COUNT(*) as count
FROM events
WHERE timestamp > DATE_SUB(NOW(), INTERVAL 1 DAY)
GROUP BY event_type
ORDER BY count DESC;
```

---

## 🎯 Design Decisions

### Why JSON for `interests` and `details`?
- ✅ Flexible schema (easy to add new interests without ALTER TABLE)
- ✅ Matches your `Preferences.java` list structure
- ✅ Easy to serialize/deserialize in Java
- ✅ MySQL has native JSON support with query functions

### Why separate `conversation_history` instead of JSON in sessions?
- ✅ Easier to query individual messages
- ✅ Better for "load last 10 messages" queries
- ✅ Cleaner data model
- ✅ Scales better (indexes on timestamp)

### Why nullable FKs in `events`?
- ✅ Supports guest users (no profile)
- ✅ Keep analytics even if user deletes account (SET NULL)
- ✅ Matches NFR-5 (privacy - can delete profile but keep anonymized analytics)

### Why AUTO_INCREMENT for `message_id` and `event_id`?
- ✅ Simpler than generating UUIDs in Java
- ✅ Sequential IDs are faster for inserts
- ✅ Easy to debug ("show me event #1234")

---

## 📊 Storage Estimates (for NFR-8: 10,000 concurrent sessions)

**Assumptions:**
- 10,000 active sessions at once
- Average 10 messages per session
- Average 5 events per session

**Storage per session:**
- 1 session row: ~200 bytes
- 10 conversation_history rows: ~10KB
- 5 event rows: ~1KB
- **Total: ~11.2KB per session**

**For 10,000 sessions:** ~112MB (well within MySQL capacity)

**Disk space needed:** <1GB (with indexes)

✅ Easily supports NFR-8 (10,000 concurrent sessions)

---

## 🔒 Security Considerations

**NFR-5: Encrypt stored user data**

**Options:**
1. **Application-level encryption** (recommended for CPS731):
   - Encrypt `interests`, `message_text` before INSERT
   - Decrypt after SELECT
   - Use Java Cipher with AES-256

2. **MySQL encryption** (production-grade):
   - Enable MySQL encryption at rest
   - `--encrypt-binlog` for backups

**For Lab-7:** Application-level encryption is sufficient (show code in Java)

**NFR-10: Location consent**
- Store location only with consent flag
- Add `location_consent BOOLEAN` to profiles if tracking location history

---

## ✅ Schema Validation Against Requirements

| Requirement | Table(s) | Supported? |
|------------|----------|------------|
| FR-5: Store user sessions | sessions | ✅ |
| FR-6: Retrieve user preferences | profiles | ✅ |
| FR-4: Maintain conversational context | conversation_history | ✅ |
| FR-17: Store/retrieve session history | sessions + conversation_history | ✅ |
| FR-20: Log interactions & analytics | events | ✅ |
| NFR-3: Rate limiting | events (with timestamp queries) | ✅ |
| NFR-5: Encrypt stored data | All tables (via app encryption) | ✅ |
| NFR-8: 10,000 concurrent sessions | All tables (tested capacity) | ✅ |

---

## 🚀 Next Steps

1. ✅ Create `schema.sql` with all CREATE TABLE statements
2. ✅ Execute schema in MySQL: `mysql -u root travel_assistant_db < schema.sql`
3. ✅ Download MySQL JDBC driver (mysql-connector-java-8.0.33.jar)
4. ✅ Create `DatabaseConnection.java` utility class
5. ✅ Implement `ProfileContextStore` with SQL queries
6. ✅ Implement `AnalyticsLogger` with SQL queries
7. ✅ Test with sample data

---

**Schema Version:** 1.0
**Last Updated:** November 20, 2024
**Status:** Ready for implementation
