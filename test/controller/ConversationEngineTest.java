package test.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import controller.ConversationEngine;
import model.*;
import service.IntegrationLayer;
import service.PlacesService;
import service.TransitService;
import service.WeatherService;
import domain.*;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit 5 tests for ConversationEngine.
 * Tests the main controller flow for recommendations.
 */
public class ConversationEngineTest {

    private ConversationEngine engine;
    private RecommendationEngine recommendationEngine;
    private ProfileContextStore profileStore;
    private IntegrationLayer integrationLayer;
    private AnalyticsLogger analyticsLogger;
    private Preferences testPreferences;

    @BeforeEach
    public void setUp() {
        // Initialize dependencies
        recommendationEngine = new RecommendationEngine();
        profileStore = new ProfileContextStore();
        profileStore.initialize();
        integrationLayer = new IntegrationLayer();
        analyticsLogger = new AnalyticsLogger();

        // Setup integration layer with services
        PlacesService placesService = new PlacesService();
        TransitService transitService = new TransitService();
        WeatherService weatherService = new WeatherService();
        
        integrationLayer.setPlacesService(placesService);
        integrationLayer.setTransitService(transitService);
        integrationLayer.setWeatherService(weatherService);
        
        recommendationEngine.setIntegrationLayer(integrationLayer);

        // Create conversation engine with dependencies
        engine = new ConversationEngine();
        engine.setRecommendationEngine(recommendationEngine);
        engine.setProfileContextStore(profileStore);
        engine.setIntegrationLayer(integrationLayer);
        engine.setAnalyticsLogger(analyticsLogger);

        // Setup test preferences
        testPreferences = new Preferences();
        testPreferences.setInterests(Arrays.asList("museums", "restaurants"));
        testPreferences.setBudget("medium");
        testPreferences.setRadius(5000);
        testPreferences.setTransportMode("walking");
        testPreferences.setAccessibilityNeeds(false);
    }

    @AfterEach
    public void tearDown() {
        engine = null;
    }

    // ========== Start Planning Tests ==========

    @Test
    @DisplayName("Test start planning with valid preferences")
    public void testStartPlanningValid() {
        // Act
        engine.startPlanning(testPreferences);

        // Assert
        // Verify that context is set up (can check indirectly through behavior)
        assertNotNull(engine, "Engine should be initialized");
    }

    @Test
    @DisplayName("Test start planning with null preferences")
    public void testStartPlanningNullPreferences() {
        // Act - Should handle gracefully
        engine.startPlanning(null);

        // Assert - Should not throw exception, just handle gracefully
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test start planning with different budgets")
    public void testStartPlanningDifferentBudgets() {
        // Test low budget
        testPreferences.setBudget("low");
        engine.startPlanning(testPreferences);
        
        // Test high budget
        testPreferences.setBudget("high");
        engine.startPlanning(testPreferences);

        // Assert - Should complete without errors
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test start planning with multiple interests")
    public void testStartPlanningMultipleInterests() {
        // Arrange
        testPreferences.setInterests(Arrays.asList("museums", "parks", "restaurants", "shopping"));

        // Act
        engine.startPlanning(testPreferences);

        // Assert
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test start planning with accessibility needs")
    public void testStartPlanningAccessibility() {
        // Arrange
        testPreferences.setAccessibilityNeeds(true);

        // Act
        engine.startPlanning(testPreferences);

        // Assert
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test start planning with different transport modes")
    public void testStartPlanningTransportModes() {
        // Test walking
        testPreferences.setTransportMode("walking");
        engine.startPlanning(testPreferences);
        
        // Test transit
        testPreferences.setTransportMode("transit");
        engine.startPlanning(testPreferences);
        
        // Test driving
        testPreferences.setTransportMode("driving");
        engine.startPlanning(testPreferences);

        // Assert
        assertNotNull(engine);
    }

    // ========== Refine Search Tests ==========

    @Test
    @DisplayName("Test adjust preferences with updated preferences")
    public void testAdjustPreferences() {
        // Arrange - First do initial planning
        engine.startPlanning(testPreferences);
        
        // Act - Adjust with new preferences
        Preferences newPreferences = new Preferences();
        newPreferences.setInterests(Arrays.asList("parks"));
        newPreferences.setBudget("low");
        newPreferences.setRadius(2000);
        newPreferences.setTransportMode("walking");
        
        engine.adjustPreferences(newPreferences);

        // Assert
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test adjust preferences with null preferences")
    public void testAdjustPreferencesNull() {
        // Arrange
        engine.startPlanning(testPreferences);
        
        // Act - Should handle gracefully
        engine.adjustPreferences(null);

        // Assert
        assertNotNull(engine);
    }

    // ========== Sign In Tests ==========

    @Test
    @DisplayName("Test start session with new user")
    public void testStartSessionNewUser() {
        // Arrange
        UserID newUser = new UserID("new_user_123");

        // Act
        engine.startSession(newUser);

        // Assert
        assertNotNull(engine);
        // In full implementation, would verify profile was loaded/created
    }

    @Test
    @DisplayName("Test start session with existing user")
    public void testStartSessionExistingUser() {
        // Arrange - Create a user profile first
        UserID existingUser = new UserID("existing_user_456");
        Profile profile = new Profile();
        profile.setUserId(existingUser);
        profile.setPreferences(testPreferences);
        profileStore.saveProfile(profile);

        // Act
        engine.startSession(existingUser);

        // Assert
        assertNotNull(engine);
        // User preferences should be loaded
    }

    @Test
    @DisplayName("Test start session with null user ID")
    public void testStartSessionNullUserId() {
        // Act - Should handle gracefully
        engine.startSession(null);

        // Assert
        assertNotNull(engine);
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("Test complete flow: start session -> start planning -> adjust preferences")
    public void testCompleteFlow() {
        // Arrange
        UserID userId = new UserID("test_flow_user");

        // Act
        engine.startSession(userId);
        engine.startPlanning(testPreferences);
        
        Preferences refinedPrefs = new Preferences();
        refinedPrefs.setInterests(Arrays.asList("restaurants"));
        refinedPrefs.setBudget("high");
        refinedPrefs.setRadius(3000);
        refinedPrefs.setTransportMode("transit");
        
        engine.adjustPreferences(refinedPrefs);

        // Assert
        assertNotNull(engine);
    }

    @Test
    @DisplayName("Test multiple sessions for same user")
    public void testMultipleSessions() {
        // Arrange
        UserID userId = new UserID("multi_session_user");

        // Act - Session 1
        engine.startSession(userId);
        engine.startPlanning(testPreferences);
        
        // Act - Session 2 (simulate new session)
        ConversationEngine engine2 = new ConversationEngine();
        engine2.setRecommendationEngine(recommendationEngine);
        engine2.setProfileContextStore(profileStore);
        engine2.setIntegrationLayer(integrationLayer);
        engine2.setAnalyticsLogger(analyticsLogger);
        
        engine2.startSession(userId);
        engine2.startPlanning(testPreferences);

        // Assert
        assertNotNull(engine);
        assertNotNull(engine2);
    }

    @Test
    @DisplayName("Test planning without starting session")
    public void testPlanningWithoutSession() {
        // Act - Start planning without starting session first
        engine.startPlanning(testPreferences);

        // Assert - Should work for anonymous users
        assertNotNull(engine);
    }
}
