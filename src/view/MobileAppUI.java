package view;

import model.*;
import domain.*;
import java.util.List;

/**
 * «view» Mobile UI layer.
 */
public class MobileAppUI {

    private String currentSessionId;
    private String currentLocation;
    private List<RecommendationCard> displayedCards;

    public Preferences capturePreferences() {
        // TODO: capture user preferences from UI
        return null;
    }

    public void requestLocation() {
        // TODO: trigger GPS or manual location flow
    }

    public void renderCards(List<RecommendationCard> cards) {
        this.displayedCards = cards;
        // TODO: render list of cards on screen
    }

    public void showSteps(Itinerary itinerary) {
        // TODO: display itinerary steps to the user
    }

    public void showMessage(String msg) {
        // TODO: show generic message (errors, rate limit, etc.)
    }
}
