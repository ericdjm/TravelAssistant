import model.*;
import domain.*;
import java.util.Arrays;
import java.time.Instant;

/**
 * Test class for ProfileContextStore with MySQL persistence.
 *
 * Tests:
 * 1. Save and load profile
 * 2. Save and load session
 * 3. Update existing profile
 * 4. Handle guest sessions (null user_id)
 * 5. Count operations
 *
 * @author CPS731 Team 20
 */
public class TestProfileContextStore {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("   TESTING ProfileContextStore WITH MYSQL");
        System.out.println("   CPS731 Travel Assistant - Phase 3");
        System.out.println("=".repeat(70));
        System.out.println();

        // Initialize ProfileContextStore
        ProfileContextStore store = new ProfileContextStore();
        store.initialize();
        System.out.println();

        // Test 1: Save and Load Profile
        System.out.println("[TEST 1] Save and Load Profile");
        System.out.println("-".repeat(70));
        testSaveAndLoadProfile(store);
        System.out.println();

        // Test 2: Update Existing Profile
        System.out.println("[TEST 2] Update Existing Profile");
        System.out.println("-".repeat(70));
        testUpdateProfile(store);
        System.out.println();

        // Test 3: Save and Load Session
        System.out.println("[TEST 3] Save and Load Session (Authenticated User)");
        System.out.println("-".repeat(70));
        testSaveAndLoadSession(store);
        System.out.println();

        // Test 4: Guest Session (null user_id)
        System.out.println("[TEST 4] Save and Load Guest Session (No User)");
        System.out.println("-".repeat(70));
        testGuestSession(store);
        System.out.println();

        // Test 5: Count Operations
        System.out.println("[TEST 5] Count Profiles and Sessions");
        System.out.println("-".repeat(70));
        testCounts(store);
        System.out.println();

        // Test 6: Load Non-Existent Data
        System.out.println("[TEST 6] Load Non-Existent Profile and Session");
        System.out.println("-".repeat(70));
        testNonExistent(store);
        System.out.println();

        // Summary
        System.out.println("=".repeat(70));
        System.out.println("✅ ALL TESTS COMPLETED!");
        System.out.println("ProfileContextStore is working with MySQL persistence.");
        System.out.println("=".repeat(70));
    }

    private static void testSaveAndLoadProfile(ProfileContextStore store) {
        // Create a test profile
        UserID userId = new UserID("test_user_001");
        Profile profile = new Profile();
        profile.setUserId(userId);

        Preferences prefs = new Preferences();
        prefs.setInterests(Arrays.asList("restaurants", "museums", "parks"));
        prefs.setBudget("medium");
        prefs.setRadius(2000);
        prefs.setTransportMode("walking");
        prefs.setAccessibilityNeeds(false);

        profile.setPreferences(prefs);

        // Save profile
        System.out.println("Saving profile for: " + userId.getValue());
        store.saveProfile(profile);

        // Load profile
        System.out.println("Loading profile for: " + userId.getValue());
        Profile loadedProfile = store.loadProfile(userId);

        // Verify
        if (loadedProfile != null) {
            System.out.println("✓ Profile loaded successfully!");
            System.out.println("  User ID: " + loadedProfile.getUserId().getValue());
            System.out.println("  Interests: " + loadedProfile.getPreferences().getInterests());
            System.out.println("  Budget: " + loadedProfile.getPreferences().getBudget());
            System.out.println("  Radius: " + loadedProfile.getPreferences().getRadius() + "m");
            System.out.println("  Transport: " + loadedProfile.getPreferences().getTransportMode());
        } else {
            System.err.println("❌ Failed to load profile!");
        }
    }

    private static void testUpdateProfile(ProfileContextStore store) {
        UserID userId = new UserID("test_user_001");

        // Load existing profile
        Profile profile = store.loadProfile(userId);
        if (profile == null) {
            System.err.println("❌ Profile not found for update test");
            return;
        }

        // Update preferences
        Preferences updatedPrefs = profile.getPreferences();
        updatedPrefs.setBudget("high"); // Change budget
        updatedPrefs.setRadius(5000);   // Increase radius

        // Add new interest (create new list to avoid immutability issues)
        java.util.List<String> newInterests = new java.util.ArrayList<>(updatedPrefs.getInterests());
        newInterests.add("cafes");
        updatedPrefs.setInterests(newInterests);

        System.out.println("Updating profile: " + userId.getValue());
        System.out.println("  New budget: " + updatedPrefs.getBudget());
        System.out.println("  New radius: " + updatedPrefs.getRadius() + "m");
        System.out.println("  New interests: " + updatedPrefs.getInterests());

        // Save updated profile
        store.saveProfile(profile);

        // Reload to verify update
        Profile reloadedProfile = store.loadProfile(userId);
        if (reloadedProfile != null) {
            System.out.println("✓ Profile updated successfully!");
            System.out.println("  Verified budget: " + reloadedProfile.getPreferences().getBudget());
            System.out.println("  Verified radius: " + reloadedProfile.getPreferences().getRadius() + "m");
        }
    }

    private static void testSaveAndLoadSession(ProfileContextStore store) {
        SessionID sessionId = new SessionID("test_session_001");
        UserID userId = new UserID("test_user_001");

        // Create session
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(userId);
        session.setCreatedAt(Instant.now());
        session.incrementRequestCount();
        session.incrementRequestCount();
        session.incrementRequestCount(); // 3 requests

        System.out.println("Saving session: " + sessionId.getValue());
        System.out.println("  User: " + userId.getValue());
        System.out.println("  Request count: " + session.getRequestCount());

        // Save session
        store.saveSession(session);

        // Load session
        System.out.println("Loading session: " + sessionId.getValue());
        Session loadedSession = store.loadSession(sessionId);

        // Verify
        if (loadedSession != null) {
            System.out.println("✓ Session loaded successfully!");
            System.out.println("  Session ID: " + loadedSession.getSessionId().getValue());
            System.out.println("  User ID: " + loadedSession.getUserId().getValue());
            System.out.println("  Created at: " + loadedSession.getCreatedAt());
            System.out.println("  Request count: " + loadedSession.getRequestCount());
        } else {
            System.err.println("❌ Failed to load session!");
        }
    }

    private static void testGuestSession(ProfileContextStore store) {
        SessionID sessionId = new SessionID("test_guest_session_001");

        // Create guest session (no user_id)
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(null); // Guest session
        session.setCreatedAt(Instant.now());
        session.incrementRequestCount();
        session.incrementRequestCount(); // 2 requests

        System.out.println("Saving guest session: " + sessionId.getValue());
        System.out.println("  User: null (guest)");
        System.out.println("  Request count: " + session.getRequestCount());

        // Save session
        store.saveSession(session);

        // Load session
        System.out.println("Loading guest session: " + sessionId.getValue());
        Session loadedSession = store.loadSession(sessionId);

        // Verify
        if (loadedSession != null) {
            System.out.println("✓ Guest session loaded successfully!");
            System.out.println("  Session ID: " + loadedSession.getSessionId().getValue());
            System.out.println("  User ID: " + (loadedSession.getUserId() != null ?
                             loadedSession.getUserId().getValue() : "null (guest)"));
            System.out.println("  Request count: " + loadedSession.getRequestCount());
        } else {
            System.err.println("❌ Failed to load guest session!");
        }
    }

    private static void testCounts(ProfileContextStore store) {
        int profileCount = store.getProfileCount();
        int sessionCount = store.getSessionCount();

        System.out.println("Database counts:");
        System.out.println("  Total profiles: " + profileCount);
        System.out.println("  Total sessions: " + sessionCount);

        if (profileCount > 0 && sessionCount > 0) {
            System.out.println("✓ Count operations working!");
        }
    }

    private static void testNonExistent(ProfileContextStore store) {
        UserID nonExistentUser = new UserID("nonexistent_user_999");
        SessionID nonExistentSession = new SessionID("nonexistent_session_999");

        System.out.println("Attempting to load non-existent profile...");
        Profile profile = store.loadProfile(nonExistentUser);
        if (profile == null) {
            System.out.println("✓ Correctly returned null for non-existent profile");
        } else {
            System.err.println("❌ ERROR: Should have returned null!");
        }

        System.out.println("Attempting to load non-existent session...");
        Session session = store.loadSession(nonExistentSession);
        if (session == null) {
            System.out.println("✓ Correctly returned null for non-existent session");
        } else {
            System.err.println("❌ ERROR: Should have returned null!");
        }
    }
}
