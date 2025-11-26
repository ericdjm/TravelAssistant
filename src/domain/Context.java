package domain;

import java.time.LocalDateTime;

/**
 * Session context holding current state.
 */
public class Context {
    private LatLng currentLocation;
    private LocalDateTime timestamp;
    private Weather currentWeather;
    private String timeOfDay;  // "morning", "afternoon", "evening", "night"

    public Context() {
    }

    public Context(LatLng currentLocation, LocalDateTime timestamp, Weather currentWeather, String timeOfDay) {
        this.currentLocation = currentLocation;
        this.timestamp = timestamp;
        this.currentWeather = currentWeather;
        this.timeOfDay = timeOfDay;
    }

    // Getters and setters
    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
