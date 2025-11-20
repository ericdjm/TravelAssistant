package controller;

import model.*;
import service.*;
import domain.*;

/**
 * «controller» central application controller.
 */
public class ConversationEngine {

    private Context sessionContext;
    private String promptState;

    // Dependencies from the class diagram
    private RecommendationEngine recommendationEngine;
    private ProfileContextStore profileContextStore;
    private IntegrationLayer integrationLayer;
    private AnalyticsLogger analyticsLogger;

    public void startPlanning(Preferences prefs) {
        // TODO: entry point when user wants to plan now
    }

    public void adjustPreferences(Preferences delta) {
        // TODO: update preferences and rerun recommendation
    }

    public void handleShowMore() {
        // TODO: show more results
    }

    public void handleSelectCard(String cardId) {
        // TODO: show micro itinerary for selected card
    }

    public Profile loadProfile(UserID userId) {
        return profileContextStore.loadProfile(userId);
    }

    public void saveProfile(Profile profile) {
        profileContextStore.saveProfile(profile);
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
}
