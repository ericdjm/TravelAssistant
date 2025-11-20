package model;

import java.net.URI;

/**
 * «entity» value object representing a place card.
 */
public class RecommendationCard {

    private String placeId;
    private String name;
    private float rating;
    private float distance;
    private String summary;

    public void openDetails() {
        // TODO: maybe navigate to details
    }

    public URI mapLink() {
        // TODO: construct and return a map URI
        return null;
    }

    // Getters and setters
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
