package model;

import domain.*;
import service.IntegrationLayer;
import java.util.List;

/**
 * «entity» core recommendation logic.
 */
public class RecommendationEngine {

    private List<RecommendationCard> cachedResults;
    private IntegrationLayer integrationLayer;

    public List<POI> fetchCandidates(Query query) {
        // TODO: call integration layer to get raw POIs
        return null;
    }

    public List<RecommendationCard> rankPOIs(Preferences prefs, Context ctx) {
        // TODO: convert POIs to cards, apply ranking
        return null;
    }

    public Itinerary buildMicroItinerary(Preferences prefs, RecommendationCard card) {
        // TODO: build itinerary for a given card
        return null;
    }

    public void setIntegrationLayer(IntegrationLayer integrationLayer) {
        this.integrationLayer = integrationLayer;
    }
}
