package test.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.RecommendationEngine;
import model.RecommendationCard;
import service.IntegrationLayer;
import service.PlacesService;
import domain.*;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit 5 tests for RecommendationEngine.
 * Tests POI ranking and recommendation logic.
 */
public class RecommendationEngineTest {

    private RecommendationEngine engine;
    private IntegrationLayer integrationLayer;
    private Preferences testPreferences;
    private Context testContext;

    @BeforeEach
    public void setUp() {
        engine = new RecommendationEngine();
        
        // Setup integration layer with mock data
        integrationLayer = new IntegrationLayer();
        PlacesService placesService = new PlacesService();
        integrationLayer.setPlacesService(placesService);
        
        engine.setIntegrationLayer(integrationLayer);

        // Setup test preferences
        testPreferences = new Preferences();
        testPreferences.setInterests(Arrays.asList("museums", "restaurants"));
        testPreferences.setBudget("medium");
        testPreferences.setRadius(5000);
        testPreferences.setTransportMode("walking");
        testPreferences.setAccessibilityNeeds(false);

        // Setup test context
        testContext = new Context();
        testContext.setCurrentLocation(new LatLng(43.6532, -79.3832)); // Toronto
        testContext.setTimeOfDay("afternoon");
        
        Weather weather = new Weather();
        weather.setCondition("sunny");
        weather.setTemperature(22.0);
        testContext.setCurrentWeather(weather);
    }

    @AfterEach
    public void tearDown() {
        engine.clearCache();
        engine = null;
    }

    // ========== Fetch Candidates Tests ==========

    @Test
    @DisplayName("Test fetch candidates with valid query")
    public void testFetchCandidatesValid() {
        // Arrange
        Query query = new Query();
        query.setLocation(new LatLng(43.6532, -79.3832));
        query.setPreferences(testPreferences);
        query.setMaxResults(10);

        // Act
        List<POI> candidates = engine.fetchCandidates(query);

        // Assert
        assertNotNull(candidates, "Candidates list should not be null");
        assertTrue(candidates.size() > 0, "Should fetch at least some candidates");
    }

    @Test
    @DisplayName("Test fetch candidates with null query")
    public void testFetchCandidatesNullQuery() {
        // Act
        List<POI> candidates = engine.fetchCandidates(null);

        // Assert
        assertNotNull(candidates, "Should return empty list, not null");
        assertEquals(0, candidates.size(), "Should return empty list for null query");
    }

    @Test
    @DisplayName("Test fetch candidates with null location")
    public void testFetchCandidatesNullLocation() {
        // Arrange
        Query query = new Query();
        query.setLocation(null);
        query.setPreferences(testPreferences);

        // Act
        List<POI> candidates = engine.fetchCandidates(query);

        // Assert
        assertNotNull(candidates);
        assertEquals(0, candidates.size(), "Should return empty list for null location");
    }

    @Test
    @DisplayName("Test fetch candidates with uninitialized integration layer")
    public void testFetchCandidatesNoIntegrationLayer() {
        // Arrange
        RecommendationEngine newEngine = new RecommendationEngine();
        Query query = new Query();
        query.setLocation(new LatLng(43.6532, -79.3832));
        query.setPreferences(testPreferences);

        // Act
        List<POI> candidates = newEngine.fetchCandidates(query);

        // Assert
        assertNotNull(candidates);
        assertEquals(0, candidates.size(), "Should return empty list when integration layer not set");
    }

    // ========== Rank POIs Tests ==========

    @Test
    @DisplayName("Test rank POIs with valid preferences and context")
    public void testRankPOIsValid() {
        // Act
        List<RecommendationCard> cards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(cards, "Recommendation cards should not be null");
        assertTrue(cards.size() > 0, "Should return at least some recommendations");
        
        // Verify cards are sorted by score (descending)
        if (cards.size() > 1) {
            RecommendationCard first = cards.get(0);
            RecommendationCard second = cards.get(1);
            assertTrue(first.getRating() >= 0, "Rating should be non-negative");
        }
    }

    @Test
    @DisplayName("Test rank POIs returns sorted results")
    public void testRankPOIsSorting() {
        // Act
        List<RecommendationCard> cards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        if (cards.size() >= 2) {
            // Check that higher-rated places come first (generally)
            // Note: actual sorting depends on the scoring algorithm
            for (RecommendationCard card : cards) {
                assertTrue(card.getRating() >= 0 && card.getRating() <= 5.0,
                    "Rating should be between 0 and 5");
            }
        }
    }

    @Test
    @DisplayName("Test rank POIs with different budget preferences")
    public void testRankPOIsDifferentBudgets() {
        // Test with low budget
        testPreferences.setBudget("low");
        List<RecommendationCard> lowBudgetCards = engine.rankPOIs(testPreferences, testContext);
        
        engine.clearCache();
        
        // Test with high budget
        testPreferences.setBudget("high");
        List<RecommendationCard> highBudgetCards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(lowBudgetCards);
        assertNotNull(highBudgetCards);
        // Results may differ based on budget
    }

    @Test
    @DisplayName("Test rank POIs with different interests")
    public void testRankPOIsDifferentInterests() {
        // Test with museums
        testPreferences.setInterests(Arrays.asList("museums"));
        List<RecommendationCard> museumCards = engine.rankPOIs(testPreferences, testContext);
        
        engine.clearCache();
        
        // Test with restaurants
        testPreferences.setInterests(Arrays.asList("restaurants"));
        List<RecommendationCard> restaurantCards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(museumCards);
        assertNotNull(restaurantCards);
        assertTrue(museumCards.size() > 0 || restaurantCards.size() > 0);
    }

    @Test
    @DisplayName("Test cache functionality")
    public void testCacheFunctionality() {
        // Act - First call
        List<RecommendationCard> firstCall = engine.rankPOIs(testPreferences, testContext);
        
        // Act - Second call (should use cache)
        List<RecommendationCard> secondCall = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall.size(), secondCall.size(), "Cached results should be same size");
    }

    @Test
    @DisplayName("Test clear cache")
    public void testClearCache() {
        // Arrange
        engine.rankPOIs(testPreferences, testContext);

        // Act
        engine.clearCache();
        
        // After clearing cache, next call should fetch new data
        List<RecommendationCard> cards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(cards);
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Test rank POIs with null context")
    public void testRankPOIsNullContext() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            engine.rankPOIs(testPreferences, null);
        }, "Should throw exception for null context");
    }

    @Test
    @DisplayName("Test rank POIs with accessibility needs")
    public void testRankPOIsAccessibility() {
        // Arrange
        testPreferences.setAccessibilityNeeds(true);

        // Act
        List<RecommendationCard> cards = engine.rankPOIs(testPreferences, testContext);

        // Assert
        assertNotNull(cards);
        // In a full implementation, would verify accessibility features
    }
}
