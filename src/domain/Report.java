package domain;

import java.util.Map;

/**
 * Analytics report.
 */
public class Report {
    private int totalEvents;
    private Map<String, Integer> eventCounts;  // event type -> count
    private Map<UserID, Integer> userActivity; // user -> request count
    private String generatedAt;

    public Report() {
    }

    public Report(int totalEvents, Map<String, Integer> eventCounts, Map<UserID, Integer> userActivity, String generatedAt) {
        this.totalEvents = totalEvents;
        this.eventCounts = eventCounts;
        this.userActivity = userActivity;
        this.generatedAt = generatedAt;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Map<String, Integer> getEventCounts() {
        return eventCounts;
    }

    public void setEventCounts(Map<String, Integer> eventCounts) {
        this.eventCounts = eventCounts;
    }

    public Map<UserID, Integer> getUserActivity() {
        return userActivity;
    }

    public void setUserActivity(Map<UserID, Integer> userActivity) {
        this.userActivity = userActivity;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }
}
