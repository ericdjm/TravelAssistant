package domain;

import java.util.List;

/**
 * User preferences for recommendations.
 */
public class Preferences {
    private List<String> interests;      // e.g., "museums", "restaurants", "parks"
    private String budget;                // e.g., "low", "medium", "high"
    private int radius;                   // search radius in meters
    private String transportMode;         // e.g., "walking", "driving", "transit"
    private boolean accessibilityNeeds;   // accessibility requirements

    public Preferences() {
    }

    public Preferences(List<String> interests, String budget, int radius, String transportMode, boolean accessibilityNeeds) {
        this.interests = interests;
        this.budget = budget;
        this.radius = radius;
        this.transportMode = transportMode;
        this.accessibilityNeeds = accessibilityNeeds;
    }

    // Getters and setters
    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public boolean isAccessibilityNeeds() {
        return accessibilityNeeds;
    }

    public void setAccessibilityNeeds(boolean accessibilityNeeds) {
        this.accessibilityNeeds = accessibilityNeeds;
    }
}
