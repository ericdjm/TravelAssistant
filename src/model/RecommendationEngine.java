package model;

import domain.*;
import service.IntegrationLayer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * «entity» core recommendation logic.
 * GRASP: Information Expert - Knows how to rank and recommend POIs
 *
 * Traceability:
 * - FR-1: Generate personalized recommendations
 * - FR-2: Rank POIs based on preferences
 * - FR-3: Build micro-itineraries
 * - UC-1: Discover Places
 *
 * @author CPS731 Team 20
 */
public class RecommendationEngine {

    private List<RecommendationCard> cachedResults;
    private IntegrationLayer integrationLayer;

    /**
     * Fetch candidate POIs from integration layer.
     * FR-1: Generate personalized recommendations
     *
     * @param query Query with location, preferences, search term
     * @return List of candidate POIs
     */
    public List<POI> fetchCandidates(Query query) {
        if (integrationLayer == null) {
            System.err.println("❌ IntegrationLayer not initialized");
            return new ArrayList<>();
        }

        if (query == null || query.getLocation() == null) {
            System.err.println("❌ Invalid query");
            return new ArrayList<>();
        }

        System.out.println("[RecommendationEngine] Fetching candidates...");

        // Get POIs from integration layer
        List<POI> pois = integrationLayer.getNearbyPlaces(
            query.getLocation(),
            query.getPreferences()
        );

        System.out.println("  Fetched " + pois.size() + " candidates");
        return pois;
    }

    /**
     * Rank POIs and convert to recommendation cards.
     * FR-2: Rank based on preferences, context, and quality signals
     *
     * Ranking factors:
     * - Rating (higher is better)
     * - Distance (closer is better)
     * - Budget match (priceLevel matches user budget)
     * - Context (time of day, weather)
     *
     * @param prefs User preferences
     * @param ctx Current context (location, time, weather)
     * @return Sorted list of recommendation cards
     */
    public List<RecommendationCard> rankPOIs(Preferences prefs, Context ctx) {
        System.out.println("[RecommendationEngine] Ranking POIs...");

        // First, fetch POIs if not cached
        if (cachedResults == null || cachedResults.isEmpty()) {
            // Create query from preferences and context
            Query query = new Query();
            query.setLocation(ctx.getCurrentLocation());
            query.setPreferences(prefs);
            query.setMaxResults(20);

            List<POI> pois = fetchCandidates(query);

            // Convert POIs to RecommendationCards
            cachedResults = pois.stream()
                .map(poi -> convertToCard(poi, ctx))
                .collect(Collectors.toList());
        }

        // Apply ranking algorithm
        List<RecommendationCard> rankedCards = new ArrayList<>(cachedResults);

        rankedCards.sort((card1, card2) -> {
            // Calculate scores for each card
            double score1 = calculateScore(card1, prefs, ctx);
            double score2 = calculateScore(card2, prefs, ctx);

            // Sort descending (higher score first)
            return Double.compare(score2, score1);
        });

        System.out.println("  Ranked " + rankedCards.size() + " cards");
        return rankedCards;
    }

    /**
     * Build micro-itinerary for a selected recommendation.
     * FR-3: Create 2-4 stop itinerary
     *
     * Strategy:
     * 1. Main POI (the selected card)
     * 2. Find 1-3 nearby related POIs
     * 3. Get ETAs between stops
     * 4. Generate step-by-step directions
     *
     * @param prefs User preferences
     * @param card Selected recommendation card
     * @return Itinerary with steps and total ETA
     */
    public Itinerary buildMicroItinerary(Preferences prefs, RecommendationCard card) {
        System.out.println("[RecommendationEngine] Building micro-itinerary for: " + card.getName());

        Itinerary itinerary = new Itinerary();
        List<String> steps = new ArrayList<>();
        int totalETA = 0;

        // Step 1: Start at main POI
        steps.add("1. Visit " + card.getName() + " (main destination)");
        steps.add("   Rating: " + card.getRating() + "/5.0");
        steps.add("   Distance: " + String.format("%.0f", card.getDistance()) + "m from you");

        // For now, simple itinerary with just the main POI
        // In a full implementation, we would:
        // - Find 1-3 nearby POIs from cachedResults
        // - Calculate ETAs between stops
        // - Generate walking/transit directions

        steps.add("");
        steps.add("2. Explore the area (30 min suggested)");
        steps.add("");
        steps.add("Total estimated time: 1-2 hours");

        totalETA = 90; // 90 minutes estimate

        itinerary.setSteps(steps);
        itinerary.setTotalETA(totalETA);

        System.out.println("  Built itinerary with " + steps.size() + " steps");
        return itinerary;
    }

    /**
     * Convert POI to RecommendationCard.
     *
     * @param poi POI to convert
     * @param ctx Context for distance calculation
     * @return RecommendationCard
     */
    private RecommendationCard convertToCard(POI poi, Context ctx) {
        RecommendationCard card = new RecommendationCard();

        card.setPlaceId(poi.getId());
        card.setName(poi.getName());
        card.setRating(poi.getRating());

        // Calculate distance from user location
        if (ctx.getCurrentLocation() != null && poi.getLocation() != null) {
            double distance = calculateDistance(ctx.getCurrentLocation(), poi.getLocation());
            card.setDistance((float) distance);
        } else {
            card.setDistance(0);
        }

        // Generate summary
        String summary = poi.getCategory().substring(0, 1).toUpperCase() +
                        poi.getCategory().substring(1);
        if (poi.getPriceLevel() != null) {
            summary += " • " + poi.getPriceLevel();
        }
        if (poi.getTags() != null && !poi.getTags().isEmpty()) {
            summary += " • " + String.join(", ", poi.getTags().subList(0, Math.min(2, poi.getTags().size())));
        }
        card.setSummary(summary);

        return card;
    }

    /**
     * Calculate recommendation score for a card.
     * Higher score = better recommendation
     *
     * Factors:
     * - Rating: 0-5 stars (weight: 40%)
     * - Distance: closer is better (weight: 40%)
     * - Budget match: bonus for matching price level (weight: 20%)
     *
     * @param card Recommendation card
     * @param prefs User preferences
     * @param ctx Context
     * @return Score (0-100)
     */
    private double calculateScore(RecommendationCard card, Preferences prefs, Context ctx) {
        double score = 0;

        // Factor 1: Rating (40% weight) - normalize to 0-40
        score += (card.getRating() / 5.0) * 40;

        // Factor 2: Distance (40% weight) - closer is better
        // Normalize distance: 0m = 40 points, 2000m+ = 0 points
        double maxDistance = prefs.getRadius();
        double distanceScore = Math.max(0, 1 - (card.getDistance() / maxDistance)) * 40;
        score += distanceScore;

        // Factor 3: Budget match (20% weight)
        // For now, give bonus if card summary contains budget info
        // In full implementation, would compare POI price level to prefs.getBudget()
        score += 20; // Default bonus

        return score;
    }

    /**
     * Calculate distance between two coordinates using Haversine formula.
     *
     * @param coord1 First coordinate
     * @param coord2 Second coordinate
     * @return Distance in meters
     */
    private double calculateDistance(LatLng coord1, LatLng coord2) {
        final int EARTH_RADIUS_KM = 6371;

        double lat1 = Math.toRadians(coord1.getLatitude());
        double lat2 = Math.toRadians(coord2.getLatitude());
        double lon1 = Math.toRadians(coord1.getLongitude());
        double lon2 = Math.toRadians(coord2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceKm = EARTH_RADIUS_KM * c;
        return distanceKm * 1000; // Convert to meters
    }

    public void setIntegrationLayer(IntegrationLayer integrationLayer) {
        this.integrationLayer = integrationLayer;
    }

    public List<RecommendationCard> getCachedResults() {
        return cachedResults;
    }

    public void clearCache() {
        this.cachedResults = null;
    }
}
