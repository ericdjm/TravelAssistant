package controller;

import model.*;
import service.*;
import domain.*;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * «controller» central application controller.
 * GRASP: Controller - Coordinates the recommendation flow
 *
 * Traceability:
 * - UC-1: Discover Places (main flow)
 * - UC-2: Refine Results
 * - UC-4: Sign in / Personalize
 * - FR-1: Generate recommendations
 * - FR-4: Allow preference adjustments
 *
 * @author CPS731 Team 20
 */
public class ConversationEngine {

    private Context sessionContext;
    private String promptState;
    private Preferences currentPreferences;
    private List<RecommendationCard> currentRecommendations;
    private Session currentSession;
    private UserID currentUserId;

    // Dependencies from the class diagram
    private RecommendationEngine recommendationEngine;
    private ProfileContextStore profileContextStore;
    private IntegrationLayer integrationLayer;
    private AnalyticsLogger analyticsLogger;

    /**
     * Main entry point: Start planning flow.
     * UC-1: Discover Places
     *
     * Flow:
     * 1. Initialize session context (location, weather, time)
     * 2. Call recommendation engine to rank POIs
     * 3. Cache results for UI display
     * 4. Update prompt state to show recommendations
     *
     * @param prefs User preferences (interests, budget, radius, etc.)
     */
    public void startPlanning(Preferences prefs) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("   STARTING PLANNING SESSION");
        System.out.println("=".repeat(70));

        if (prefs == null) {
            System.err.println("❌ Preferences cannot be null");
            return;
        }

        // Store preferences
        this.currentPreferences = prefs;

        // Clear any cached results from previous searches
        recommendationEngine.clearCache();

        // Step 1: Initialize context
        System.out.println("\n[ConversationEngine] Initializing session context...");
        sessionContext = new Context();

        // Get user location (from integration layer geocoding)
        LatLng userLocation = integrationLayer.geocode("Toronto, ON");
        sessionContext.setCurrentLocation(userLocation);
        System.out.println("  Location: " + userLocation);

        // Get weather
        Weather weather = integrationLayer.getWeather(userLocation);
        sessionContext.setCurrentWeather(weather);
        System.out.println("  Weather: " + weather);

        // Determine time of day
        LocalDateTime now = LocalDateTime.now();
        sessionContext.setTimestamp(now);
        int hour = now.getHour();
        String timeOfDay;
        if (hour < 12) {
            timeOfDay = "morning";
        } else if (hour < 17) {
            timeOfDay = "afternoon";
        } else if (hour < 21) {
            timeOfDay = "evening";
        } else {
            timeOfDay = "night";
        }
        sessionContext.setTimeOfDay(timeOfDay);
        System.out.println("  Time of day: " + timeOfDay);

        // Step 2: Get recommendations from engine
        System.out.println("\n[ConversationEngine] Getting recommendations...");
        currentRecommendations = recommendationEngine.rankPOIs(prefs, sessionContext);

        // Step 3: Update prompt state
        promptState = "showing_recommendations";

        // Step 4: Increment session request count
        incrementSessionRequests();

        System.out.println("\n✅ Planning session started!");
        System.out.println("  Found " + currentRecommendations.size() + " recommendations");
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Adjust user preferences and re-rank results.
     * UC-2: Refine Results
     * FR-4: Allow preference adjustments
     *
     * Flow:
     * 1. Merge delta preferences into current preferences
     * 2. Clear cached results in recommendation engine
     * 3. Re-run ranking with new preferences
     * 4. Update UI with new results
     *
     * @param delta Preference changes (partial update)
     */
    public void adjustPreferences(Preferences delta) {
        System.out.println("\n[ConversationEngine] Adjusting preferences...");

        if (delta == null || currentPreferences == null) {
            System.err.println("❌ Cannot adjust preferences");
            return;
        }

        // Merge delta into current preferences
        if (delta.getInterests() != null) {
            currentPreferences.setInterests(delta.getInterests());
            System.out.println("  Updated interests: " + delta.getInterests());
        }
        if (delta.getBudget() != null) {
            currentPreferences.setBudget(delta.getBudget());
            System.out.println("  Updated budget: " + delta.getBudget());
        }
        if (delta.getRadius() > 0) {
            currentPreferences.setRadius(delta.getRadius());
            System.out.println("  Updated radius: " + delta.getRadius() + "m");
        }
        if (delta.getTransportMode() != null) {
            currentPreferences.setTransportMode(delta.getTransportMode());
            System.out.println("  Updated transport: " + delta.getTransportMode());
        }

        // Clear cached results
        recommendationEngine.clearCache();

        // Re-run ranking
        currentRecommendations = recommendationEngine.rankPOIs(currentPreferences, sessionContext);

        System.out.println("✅ Preferences adjusted! New results: " + currentRecommendations.size());
    }

    /**
     * Show more recommendation results.
     * Simply returns current recommendations (could be expanded to pagination).
     */
    public void handleShowMore() {
        System.out.println("\n[ConversationEngine] Showing more results...");

        if (currentRecommendations == null || currentRecommendations.isEmpty()) {
            System.out.println("  No more results available");
            return;
        }

        // For now, just indicate we're showing the same results
        // In a full implementation, we could fetch more POIs or paginate
        System.out.println("  Showing all " + currentRecommendations.size() + " results");
    }

    /**
     * Handle card selection - show micro itinerary.
     * UC-1: Discover Places (alternative flow 4.a - view itinerary)
     * FR-3: Build micro-itineraries
     *
     * Flow:
     * 1. Find selected card in cached results
     * 2. Call recommendation engine to build itinerary
     * 3. Update prompt state to show itinerary
     *
     * @param cardId ID of selected recommendation card
     */
    public void handleSelectCard(String cardId) {
        System.out.println("\n[ConversationEngine] Card selected: " + cardId);

        if (currentRecommendations == null || currentRecommendations.isEmpty()) {
            System.err.println("❌ No recommendations available");
            return;
        }

        // Find the selected card
        RecommendationCard selectedCard = null;
        for (RecommendationCard card : currentRecommendations) {
            if (card.getPlaceId().equals(cardId)) {
                selectedCard = card;
                break;
            }
        }

        if (selectedCard == null) {
            System.err.println("❌ Card not found: " + cardId);
            return;
        }

        // Build micro-itinerary
        System.out.println("  Building itinerary for: " + selectedCard.getName());
        Itinerary itinerary = recommendationEngine.buildMicroItinerary(currentPreferences, selectedCard);

        // Update prompt state
        promptState = "showing_itinerary";

        // Display itinerary steps
        System.out.println("\n📍 MICRO-ITINERARY:");
        System.out.println("-".repeat(70));
        for (String step : itinerary.getSteps()) {
            System.out.println(step);
        }
        System.out.println("-".repeat(70));
    }

    public Profile loadProfile(UserID userId) {
        if (profileContextStore == null) {
            System.err.println("⚠️ ProfileContextStore not initialized");
            return null;
        }
        return profileContextStore.loadProfile(userId);
    }

    public void saveProfile(Profile profile) {
        if (profileContextStore == null) {
            System.err.println("⚠️ ProfileContextStore not initialized");
            return;
        }
        profileContextStore.saveProfile(profile);
    }

    // Getters for UI access
    public List<RecommendationCard> getCurrentRecommendations() {
        return currentRecommendations;
    }

    public Context getSessionContext() {
        return sessionContext;
    }

    public Preferences getCurrentPreferences() {
        return currentPreferences;
    }

    public String getPromptState() {
        return promptState;
    }

    public RecommendationEngine getRecommendationEngine() {
        return recommendationEngine;
    }

    public ProfileContextStore getProfileContextStore() {
        return profileContextStore;
    }

    // Optional wiring methods (not in diagram but useful)
    public void setRecommendationEngine(RecommendationEngine recommendationEngine) {
        this.recommendationEngine = recommendationEngine;
    }

    public void setProfileContextStore(ProfileContextStore profileContextStore) {
        this.profileContextStore = profileContextStore;
    }

    public void setIntegrationLayer(IntegrationLayer integrationLayer) {
        this.integrationLayer = integrationLayer;
    }

    public void setAnalyticsLogger(AnalyticsLogger analyticsLogger) {
        this.analyticsLogger = analyticsLogger;
    }

    // ========================================================================
    // Session Management
    // ========================================================================

    /**
     * Start a new session for a user.
     * Creates a new Session object and saves it to database.
     *
     * @param userId User ID (can be null for guest)
     */
    public void startSession(UserID userId) {
        this.currentUserId = userId;

        // Create new session
        currentSession = new Session();
        currentSession.setSessionId(new SessionID("session_" + UUID.randomUUID().toString()));
        currentSession.setUserId(userId); // Can be null for guests
        currentSession.setCreatedAt(Instant.now());

        System.out.println("[ConversationEngine] New session started: " + currentSession.getSessionId().getValue());
        if (userId != null) {
            System.out.println("  User: " + userId.getValue());
        } else {
            System.out.println("  User: Guest");
        }
    }

    /**
     * Increment request count for current session.
     * Call this each time user makes a request (startPlanning, adjustPreferences, etc.)
     */
    public void incrementSessionRequests() {
        if (currentSession != null) {
            currentSession.incrementRequestCount();
        }
    }

    /**
     * Save current session to database.
     * Call this when user is done with planning or navigates away.
     */
    public void saveCurrentSession() {
        if (currentSession == null) {
            System.err.println("⚠️ No active session to save");
            return;
        }

        if (profileContextStore == null) {
            System.err.println("⚠️ ProfileContextStore not initialized");
            return;
        }

        profileContextStore.saveSession(currentSession);
        System.out.println("✓ Session saved: " + currentSession.getSessionId().getValue() +
                         " (" + currentSession.getRequestCount() + " requests)");
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public UserID getCurrentUserId() {
        return currentUserId;
    }
}
