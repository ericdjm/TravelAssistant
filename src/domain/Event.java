package domain;

import java.time.Instant;

/**
 * Analytics event.
 */
public class Event {
    private String eventType;       // e.g., "search", "select_card", "view_itinerary"
    private UserID userId;
    private Instant timestamp;
    private String details;         // JSON or string with event-specific data

    public Event() {
    }

    public Event(String eventType, UserID userId, Instant timestamp, String details) {
        this.eventType = eventType;
        this.userId = userId;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UserID getUserId() {
        return userId;
    }

    public void setUserId(UserID userId) {
        this.userId = userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
