package model;

import domain.*;
import service.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;

/**
 * «entity» Information Expert for profiles and sessions.
 * GRASP: Information Expert - Manages profile/session persistence
 *
 * Implements persistence using MySQL database.
 *
 * Traceability:
 * - FR-5: Store user sessions for returning users
 * - FR-6: Retrieve user preferences from stored profiles
 * - FR-17: Store and retrieve session history
 * - UC-4: Sign in / Personalize
 *
 * @author CPS731 Team 20 (Hussein, Eric, Omar)
 */
public class ProfileContextStore {

    // In-memory caches (for performance - optional)
    private Map<UserID, Profile> profiles;      // aggregates Profile
    private Map<SessionID, Session> sessions;   // composes Session

    // Database connection
    private DatabaseConnection dbConnection;

    /**
     * Constructor - initializes database connection
     */
    public ProfileContextStore() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.profiles = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    /**
     * Initialize the store (for backward compatibility with HashMap version).
     * Now connects to database.
     */
    public void initialize() {
        System.out.println("✓ ProfileContextStore initialized with MySQL persistence");
        // Test connection
        try (Connection conn = dbConnection.getConnection()) {
            System.out.println("✓ Database connection verified");
        } catch (SQLException e) {
            System.err.println("⚠️ Warning: Could not verify database connection");
        }
    }

    /**
     * Save profile to database (FR-6).
     * Uses INSERT ... ON DUPLICATE KEY UPDATE for upsert behavior.
     *
     * @param p Profile to save (must have userId and preferences)
     * @throws IllegalArgumentException if profile or userId is null
     */
    public void saveProfile(Profile p) {
        if (p == null || p.getUserId() == null) {
            throw new IllegalArgumentException("Profile and UserID cannot be null");
        }

        String sql = "INSERT INTO profiles (user_id, interests, budget, radius, transport_mode, accessibility_needs) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "interests = VALUES(interests), " +
                     "budget = VALUES(budget), " +
                     "radius = VALUES(radius), " +
                     "transport_mode = VALUES(transport_mode), " +
                     "accessibility_needs = VALUES(accessibility_needs)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Preferences prefs = p.getPreferences();

            // Set parameters
            stmt.setString(1, p.getUserId().getValue());

            // Handle interests - convert List<String> to JSON array string
            if (prefs != null && prefs.getInterests() != null) {
                String interestsJson = convertListToJson(prefs.getInterests());
                stmt.setString(2, interestsJson);
                stmt.setString(3, prefs.getBudget());
                stmt.setInt(4, prefs.getRadius());
                stmt.setString(5, prefs.getTransportMode());
                stmt.setBoolean(6, prefs.isAccessibilityNeeds());
            } else {
                // Handle null preferences
                stmt.setNull(2, Types.VARCHAR);
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.INTEGER);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setBoolean(6, false);
            }

            int rowsAffected = stmt.executeUpdate();
            System.out.println("✓ Profile saved: " + p.getUserId().getValue() +
                             " (" + rowsAffected + " row(s) affected)");

            // Update in-memory cache
            profiles.put(p.getUserId(), p);

        } catch (SQLException e) {
            System.err.println("❌ Error saving profile: " + e.getMessage());
            throw new RuntimeException("Failed to save profile", e);
        }
    }

    /**
     * Load profile from database (FR-6).
     *
     * @param userId User ID to load
     * @return Profile object if found, null otherwise
     */
    public Profile loadProfile(UserID userId) {
        if (userId == null) {
            return null;
        }

        // Check cache first
        if (profiles.containsKey(userId)) {
            return profiles.get(userId);
        }

        String sql = "SELECT user_id, interests, budget, radius, transport_mode, accessibility_needs " +
                     "FROM profiles WHERE user_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId.getValue());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create Profile object
                Profile profile = new Profile();
                profile.setUserId(new UserID(rs.getString("user_id")));

                // Create Preferences object
                Preferences prefs = new Preferences();

                // Parse JSON interests back to List<String>
                String interestsJson = rs.getString("interests");
                if (interestsJson != null) {
                    prefs.setInterests(convertJsonToList(interestsJson));
                }

                prefs.setBudget(rs.getString("budget"));
                prefs.setRadius(rs.getInt("radius"));
                prefs.setTransportMode(rs.getString("transport_mode"));
                prefs.setAccessibilityNeeds(rs.getBoolean("accessibility_needs"));

                profile.setPreferences(prefs);

                // Cache it
                profiles.put(userId, profile);

                System.out.println("✓ Profile loaded: " + userId.getValue());
                return profile;
            } else {
                System.out.println("⚠️ Profile not found: " + userId.getValue());
                return null;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading profile: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save session to database (FR-5, FR-17).
     * Uses INSERT ... ON DUPLICATE KEY UPDATE for upsert behavior.
     *
     * @param s Session to save
     * @throws IllegalArgumentException if session or sessionId is null
     */
    public void saveSession(Session s) {
        if (s == null || s.getSessionId() == null) {
            throw new IllegalArgumentException("Session and SessionID cannot be null");
        }

        String sql = "INSERT INTO sessions (session_id, user_id, request_count) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "last_active = CURRENT_TIMESTAMP, " +
                     "request_count = VALUES(request_count)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getSessionId().getValue());

            // user_id can be null for guest sessions
            if (s.getUserId() != null) {
                stmt.setString(2, s.getUserId().getValue());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setInt(3, s.getRequestCount());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("✓ Session saved: " + s.getSessionId().getValue() +
                             " (" + rowsAffected + " row(s) affected)");

            // Update in-memory cache
            sessions.put(s.getSessionId(), s);

        } catch (SQLException e) {
            System.err.println("❌ Error saving session: " + e.getMessage());
            throw new RuntimeException("Failed to save session", e);
        }
    }

    /**
     * Load session from database (FR-17).
     *
     * @param sessionId Session ID to load
     * @return Session object if found, null otherwise
     */
    public Session loadSession(SessionID sessionId) {
        if (sessionId == null) {
            return null;
        }

        // Check cache first
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        }

        String sql = "SELECT session_id, user_id, created_at, request_count " +
                     "FROM sessions WHERE session_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId.getValue());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create Session object
                Session session = new Session();
                session.setSessionId(new SessionID(rs.getString("session_id")));

                // user_id can be null for guest sessions
                String userIdStr = rs.getString("user_id");
                if (userIdStr != null) {
                    session.setUserId(new UserID(userIdStr));
                }

                // Convert SQL Timestamp to Instant
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    session.setCreatedAt(timestamp.toInstant());
                }

                // Request count handling
                session.incrementRequestCount(); // Reset to 0 first
                for (int i = 0; i < rs.getInt("request_count"); i++) {
                    session.incrementRequestCount();
                }

                // Cache it
                sessions.put(sessionId, session);

                System.out.println("✓ Session loaded: " + sessionId.getValue());
                return session;
            } else {
                System.out.println("⚠️ Session not found: " + sessionId.getValue());
                return null;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading session: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get count of profiles in database (for testing/demo).
     *
     * @return Number of profiles
     */
    public int getProfileCount() {
        String sql = "SELECT COUNT(*) as count FROM profiles";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error counting profiles: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get count of sessions in database (for testing/demo).
     *
     * @return Number of sessions
     */
    public int getSessionCount() {
        String sql = "SELECT COUNT(*) as count FROM sessions";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error counting sessions: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get all sessions for a specific user (FR-17).
     * Orders by most recent first.
     *
     * @param userId User ID to get sessions for
     * @return List of sessions for this user
     */
    public java.util.List<Session> getSessionsByUser(UserID userId) {
        java.util.List<Session> userSessions = new java.util.ArrayList<>();

        if (userId == null) {
            return userSessions;
        }

        String sql = "SELECT session_id, user_id, created_at, request_count " +
                     "FROM sessions WHERE user_id = ? " +
                     "ORDER BY created_at DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId.getValue());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Session session = new Session();
                session.setSessionId(new SessionID(rs.getString("session_id")));
                session.setUserId(new UserID(rs.getString("user_id")));

                // Convert SQL Timestamp to Instant
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    session.setCreatedAt(timestamp.toInstant());
                }

                // Set request count
                int count = rs.getInt("request_count");
                for (int i = 0; i < count; i++) {
                    session.incrementRequestCount();
                }

                userSessions.add(session);
            }

            System.out.println("✓ Loaded " + userSessions.size() + " sessions for user: " + userId.getValue());

        } catch (SQLException e) {
            System.err.println("❌ Error loading sessions for user: " + e.getMessage());
        }

        return userSessions;
    }

    // ========================================================================
    // Helper Methods for JSON Conversion
    // ========================================================================

    /**
     * Convert List<String> to JSON array format for MySQL.
     * Example: ["restaurants", "museums", "parks"]
     *
     * @param list List of strings
     * @return JSON array string
     */
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Convert JSON array string to List<String>.
     * Example: ["restaurants", "museums"] -> List of 2 strings
     *
     * @param json JSON array string
     * @return List of strings
     */
    private List<String> convertJsonToList(String json) {
        List<String> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return list;
        }

        // Simple JSON parsing (remove brackets and quotes, split by comma)
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (!cleaned.isEmpty()) {
            String[] items = cleaned.split(",\\s*");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    list.add(item.trim());
                }
            }
        }

        return list;
    }
}
